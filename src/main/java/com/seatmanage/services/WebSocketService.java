package com.seatmanage.services;

import com.cloudinary.api.exceptions.BadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatmanage.config.SecurityUtil;
import com.seatmanage.dto.response.ReAssignSeatDTO;
import com.seatmanage.dto.response.SeatDTO;
import com.seatmanage.dto.response.UserPrivateDTO;
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
    private final Map<String, Set<WebSocketSession>> userRoles = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage request) throws Exception {
        Map<String, String> payload = objectMapper.readValue(request.getPayload(), Map.class);
        String type = payload.get("type");
        String username = payload.get("username");
        String role = payload.get("role");
        String roomId = payload.get("value");
System.out.println("type: " + type + " username: " + username + " role: " + role + " roomId: " + roomId);
        if ("auth".equals(type) && username != null ) {
            session.getAttributes().put("username", username);
            session.getAttributes().put("role", role);
            userRoles.computeIfAbsent(role, k -> ConcurrentHashMap.newKeySet()).add(session);
        } else if (roomId != null) {
            sessionRooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
        }
        System.out.println("get role user: " + userRoles);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("🔹 WebSocket connected: " + session.getId());
        sessionRooms.computeIfAbsent("all", k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("🔸 WebSocket closed: " + session.getId());
        sessionRooms.values().forEach(sessions -> sessions.remove(session));
        userRoles.values().forEach(sessions -> sessions.remove(session));
    }

    public void sendNoticeToRoom(String roomId,String type, String message) throws IOException, BadRequest {
                 sendToSessionsNotice(sessionRooms.get(roomId), type, roomId, Map.of("message", message));
    }

    public void sendNotice(String notice) throws IOException, BadRequest {
        sendToAll("notice", Map.of("notice", notice));
    }

    public void sendSeatUpdate(String roomId, SeatDTO seat) throws BadRequest {
        sendToRoom(roomId, "seatUpdate", seat);
    }

    public void sendSeatAction(String roomId, String action, SeatDTO seat) throws BadRequest {
        sendToRoom(roomId, action, seat);
    }

    public void sendReAssignSeat(String roomId, ReAssignSeatDTO seat) throws BadRequest {
        sendToRoom(roomId, "reassignSeat", seat);
    }

    public void sendToRole(String role, String type, Object data) throws BadRequest {
        System.out.println("sendToRole: " + userRoles.get(role));
        sendToSessionsNotice(userRoles.get(role), type, role, data);
    }

    public void sendToRoom(String roomId, String type, Object data) throws BadRequest {
        sendToSessions(sessionRooms.get(roomId), type, roomId, data);
    }

    public void sendWithRole(String role, String type, Object data) throws BadRequest {
        System.out.println("sendWithRole rrrrrrrrrr: " + userRoles.get(role));
        sendToSessionsNotice(userRoles.get(role), type, role, data);
        System.out.println("sendWithRole:222 " + role + " " + type + " " + data);
    }

    private void sendToAll(String type, Object data) throws BadRequest {
        sendToSessions(sessionRooms.get("all"), type, "all", data);
    }

    private void sendToSessionsNotice(Set<WebSocketSession> sessions, String type, String target, Object data) throws BadRequest {
        if (sessions == null || sessions.isEmpty()) return;
        UserPrivateDTO userPrivateDTO = SecurityUtil.getUserPrincipal();
        if (userPrivateDTO == null) throw new BadRequest("UnAuthenticated");

        String username = userPrivateDTO.getUsername();
        try {
            String jsonMessage = objectMapper.writeValueAsString(Map.of("type", type, "value", target, "data", data));
            for (WebSocketSession session : sessions  ) {
                if (session.isOpen() && !username.equals(session.getAttributes().get("username")))  session.sendMessage(new TextMessage(jsonMessage));
            }
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    private void sendToSessions(Set<WebSocketSession> sessions, String type, String target, Object data) throws BadRequest {
        if (sessions == null || sessions.isEmpty()) return;
        UserPrivateDTO userPrivateDTO = SecurityUtil.getUserPrincipal();
        if (userPrivateDTO == null) throw new BadRequest("UnAuthenticated");

        String username = userPrivateDTO.getUsername();
        try {
            String jsonMessage = objectMapper.writeValueAsString(Map.of("type", type, "value", target, "data", data));
            for (WebSocketSession session : sessions  ) {
                if (session.isOpen())  session.sendMessage(new TextMessage(jsonMessage));
            }
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}