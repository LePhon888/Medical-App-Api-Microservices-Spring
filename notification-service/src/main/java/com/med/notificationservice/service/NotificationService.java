package com.med.firebase.service;


import com.google.firebase.messaging.*;
import com.med.firebase.dto.NotificationDTO;
import com.med.firebase.dto.NotificationRequest;
import com.med.firebase.model.Notification;
import com.med.firebase.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    public ResponseEntity<String> createNotification(NotificationRequest request) {
        try {
            Notification notification = new Notification(
                    0,
                    request.getUserId(),
                    request.getTitle(),
                    request.getBody(),
                    false,
                    request.getClickActionParams(),
                    LocalDateTime.now()
            );
            repository.save(notification);
            return ResponseEntity.ok("Create Notification Successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    public ResponseEntity<String> sendNotificationToUser(NotificationRequest request) {
        try {
            // Check for null values in notificationRequest
            if (request == null || request.getTitle() == null || request.getBody() == null
                    || request.getToken() == null) {
                return ResponseEntity.badRequest().body("Invalid notification request");
            }

            com.google.firebase.messaging.Notification notification = com.google.firebase.messaging.Notification
                    .builder()
                    .setTitle(request.getTitle())
                    .setBody(request.getBody())
                    .setImage(request.getImage())
                    .build();

            Message message = Message.builder()
                    .setToken(request.getToken())
                    .setNotification(notification)
                    .putAllData(request.getClickActionParams())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    .setDefaultSound(true)
                                    .build())
                            .build())
                    .build();

            // Sending the FCM message
            firebaseMessaging.send(message);

            // Create a notification in your application
            createNotification(request);

            // Return a meaningful success response
            return ResponseEntity.status(HttpStatus.CREATED).body("Notification sent successfully");
        } catch (Exception e) {
            // Return an error response with details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification: " + e.getMessage());
        }
    }

    public Page<NotificationDTO> getByUserId(Integer userId, Pageable pageable) {
        return this.repository.getNotificationByUserId(userId, pageable);
    }

    public ResponseEntity<String> updateReadById(Integer id) {
        Optional<Notification> notification = repository.findById(id);
        if (notification.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Notification updated = notification.get();
        updated.setIsRead(true);
        repository.save(updated);
        return ResponseEntity.ok("Notification updated successfully");
    }

    public Long countUnreadByUserId(Integer userId) {
        return this.repository.countUnreadByUserId(userId);
    }

}
