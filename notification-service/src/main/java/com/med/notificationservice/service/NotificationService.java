package com.med.notificationservice.service;


import com.google.firebase.messaging.*;
import com.med.notificationservice.dto.NotificationDTO;
import com.med.notificationservice.dto.NotificationRequest;
import com.med.notificationservice.model.Notification;
import com.med.notificationservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    public ResponseEntity<Integer> createNotification(NotificationRequest request) {
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
            Notification savedNotification = repository.save(notification);

            // Return the ID of the created notification
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNotification.getId());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1); // Return a default value for error
        }
    }


    public ResponseEntity<String> sendNotificationToUser(NotificationRequest request) {
        try {
            // Check for null values in notificationRequest
            if (request == null || request.getTitle() == null || request.getBody() == null
                    || request.getToken() == null) {
                return ResponseEntity.badRequest().body("Invalid notification request");
            }

            ResponseEntity<Integer> responseEntity = createNotification(request);

            if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                Integer notificationId = responseEntity.getBody();
                request.getClickActionParams().put("notificationId", String.valueOf(notificationId));
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
