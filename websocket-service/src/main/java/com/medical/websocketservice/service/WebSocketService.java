package com.medical.websocketservice.service;

import com.medical.websocketservice.dto.Participant;
import com.medical.websocketservice.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private Map<String, List<Participant>> roomParticipants = new ConcurrentHashMap<>();

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        Message<?> message = event.getMessage();
        MessageHeaders headers = message.getHeaders();
        // Extract userId, userName, and roomId from the headers
        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) headers.get("nativeHeaders");
        if (nativeHeaders != null && !nativeHeaders.isEmpty()) {
            String roomId = extractFirstHeaderValue(nativeHeaders, "roomId");
            String userId = extractFirstHeaderValue(nativeHeaders, "userId");
            String userName = extractFirstHeaderValue(nativeHeaders, "userName");
            String sessionId = (String) headers.get("simpSessionId");

            // Ensure roomParticipants map contains roomId as key
            roomParticipants.putIfAbsent(roomId, new ArrayList<>());

            // Get the list of participants for the roomId
            List<Participant> participants = roomParticipants.get(roomId);

            // Find the participant with the matching userId, if any
            Optional<Participant> matchingParticipant = participants.stream()
                    .filter(participant -> participant.getUserId().equals(Integer.parseInt(userId)))
                    .findFirst();

            // Update sessionId if participant exists
            matchingParticipant.ifPresent(participant -> participant.setSessionId(sessionId));

            // If the participant doesn't exist, add it to the list
            if (!matchingParticipant.isPresent()) {
                Participant participant = new Participant(Integer.parseInt(userId), userName, sessionId, LocalDateTime.now());
                participants.add(participant);

                messagingTemplate.convertAndSend("/topic/chat/" + roomId,
                        new Response<>("joinRoom", participant));

                // Send the updated list of participants to subscribers
                messagingTemplate.convertAndSend("/topic/video-call/" + roomId,
                        new Response<>("joinRoom", participant));
            }

        }
    }



    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        System.out.println("User disconnected: sessionId=" + sessionId);

        // Find the room associated with the disconnected participant's session ID
        String roomId = findRoomBySessionId(sessionId);
        if (roomId != null) {
            // Retrieve the participants in the room
            List<Participant> participants = roomParticipants.getOrDefault(roomId, new ArrayList<>());

            // Find the disconnected participant
            Optional<Participant> disconnectedParticipantOptional = participants.stream()
                    .filter(participant -> participant.getSessionId().equals(sessionId))
                    .findFirst();

            // If the disconnected participant is found, remove it from the list and send the updated participant list and notification to subscribers
            disconnectedParticipantOptional.ifPresent(disconnectedParticipant -> {
                participants.remove(disconnectedParticipant);

                messagingTemplate.convertAndSend("/topic/chat/" + roomId,
                        new Response<>("leaveRoom", disconnectedParticipant));

                messagingTemplate.convertAndSend("/topic/video-call/" + roomId,
                        new Response<>("leaveRoom", disconnectedParticipant));
            });
        }
    }

    public void sendRequestOffer(String roomId) {
        if (roomId != null) {
            List<Participant> participants = roomParticipants.get(roomId);

            if (participants.size() >= 2) {
                participants.sort((p1, p2) -> p2.getJoinedAt().compareTo(p1.getJoinedAt()));
                Participant newestParticipant = participants.get(0);

                // Decide the newest user should be the one send the offer
                messagingTemplate.convertAndSend("/topic/video-call/" + roomId,
                        new Response<>("userOffer", newestParticipant));
            }
        }
    }



    private String extractFirstHeaderValue(Map<String, List<String>> headers, String headerName) {
        List<String> values = headers.get(headerName);
        if (values != null && !values.isEmpty()) {
            return values.get(0);
        }
        return null; // Return null if header value is null or empty
    }

    private String findRoomBySessionId(String sessionId) {
        // Iterate over roomParticipants to find the room associated with the session ID
        for (Map.Entry<String, List<Participant>> entry : roomParticipants.entrySet()) {
            List<Participant> participants = entry.getValue();
            for (Participant participant : participants) {
                if (participant.getSessionId().equals(sessionId)) {
                    return entry.getKey(); // Return the room ID if found
                }
            }
        }
        return null; // Return null if room not found
    }
}
