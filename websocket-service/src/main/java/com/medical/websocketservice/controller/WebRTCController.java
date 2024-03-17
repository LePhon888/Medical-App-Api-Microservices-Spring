package com.medical.websocketservice.controller;

import com.medical.websocketservice.dto.Participant;
import com.medical.websocketservice.dto.Response;
import com.medical.websocketservice.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WebRTCController {

    private final WebSocketService service;

    @Autowired
    public WebRTCController (WebSocketService service) {
        this.service = service;
    }

    @MessageMapping("/offer/{roomId}")
    @SendTo("/topic/video-call/{roomId}")
    public String handleOffer(@Payload String message, @DestinationVariable String roomId) {
        return message;
    }

    @MessageMapping("/ready-offer/{roomId}")
    public void handleReadyOffer(@Payload String message, @DestinationVariable String roomId) {
        service.sendRequestOffer(roomId);
    }

}
