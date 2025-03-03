package com.seatmanage.controllers;

import com.seatmanage.entities.Seat;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SeatWebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    public SeatWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendSeatUpdate(Seat seat) {
        messagingTemplate.convertAndSend("/topic/seats", seat);
    }
}
