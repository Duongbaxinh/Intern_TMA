package com.seatmanage.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatmanage.dto.response.SeatDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketService extends TextWebSocketHandler {
    private final Map<String, Set<WebSocketSession>> sessionRooms = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage request) throws Exception {
        String data = request.getPayload();
        Map<String, String> payload = objectMapper.readValue(data, Map.class);
        String type = payload.get("type");
        String roomId = payload.get("value");

        sessionRooms.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
        sessionRooms.get(roomId).add(session);
        this.sendMessage(roomId,"Client " + session.getId() + " join room: " + roomId);
        session.sendMessage(new TextMessage(request.toString()));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("🔹 WebSocket connect sucessfull: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(" WebSocket đóng: " + session.getId());

        sessionRooms.forEach((roomId, sessions) -> {
            if (sessions.remove(session)) {
                if (sessions.isEmpty()) {
                    sessionRooms.remove(roomId);
                }
            }
        });

    }

    public  void sendMessage(String roomId,String message) throws IOException {
            sendMessageToRoom(roomId,"message",Map.of("message",message));
    }
    public  void sendNotice(String notice) throws IOException {
        sendMessageToAll("notice",Map.of("notice",notice));
    }
    public void sendUpdateSeatInRoom(String roomId, SeatDTO seat) {
        sendMessageToRoom(roomId, "seatUpdate", seat);
    }

    public void sendUpdateSeatAll(SeatDTO seat) {
        sendMessageToAll("seatUpdate", seat);
    }

    public void sendCreateSeat(String roomId, SeatDTO seat) {
        sendMessageToRoom( roomId,"seatCreate", seat);
    }

    public void sendDeleteSeat(String roomId, String seatId) {
        sendMessageToRoom(roomId, "seatDelete", Map.of("seatId", seatId));
    }

    private void sendMessageToRoom(String roomId, String type, Object data) {
        Set<WebSocketSession> sessions = sessionRooms.get(roomId);
        if (sessions == null || sessions.isEmpty()) {
            return;
        }

        try {
            String jsonMessage = objectMapper.writeValueAsString(Map.of("type", type, "value", roomId, "data", data));
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(jsonMessage));

                }
            }
        } catch (IOException e) {
            System.err.println( e.getMessage());
        }
    }

    private void sendMessageToAll(String type, Object data) {
        try {
            System.out.println("check payload send to all " + data);
            String jsonMessage = objectMapper.writeValueAsString(Map.of("type", type, "data", data));
            sessionRooms.values().stream()
                    .flatMap(Set::stream)
                    .filter(WebSocketSession::isOpen)
                    .forEach(session -> {
                        try {
                            session.sendMessage(new TextMessage(jsonMessage));
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.err.println( e.getMessage());
        }
    }
}
