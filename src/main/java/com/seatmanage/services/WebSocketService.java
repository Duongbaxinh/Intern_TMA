package com.seatmanage.services;

import com.seatmanage.dto.response.SeatDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendUpdatedSeat(SeatDTO seatDTO) {
        messagingTemplate.convertAndSend("/topic/user-updated", seatDTO);
    }
}
