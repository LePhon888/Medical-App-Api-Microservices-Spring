package com.med.WebSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;


@Controller
@CrossOrigin
public class ChatController {

    @Autowired
    private SimpMessageSendingOperations  messagingTemplate;

    @MessageMapping("/send/{roomId}")
    public void sendMessage(
            @DestinationVariable String roomId,
            @Payload String message) {
        // Broadcast the message to all users in the chat room
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, message);
    }
}



