package com.med.notificationservice.controller;

import com.med.notificationservice.dto.NotificationDTO;
import com.med.notificationservice.dto.NotificationRequest;
import com.med.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private NotificationService service;

    @PostMapping("/create")
    public ResponseEntity<Integer> createNotification(@RequestBody NotificationRequest request) {
        return this.service.createNotification(request);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendPushNotification(@RequestBody NotificationRequest notificationRequest) {
        return service.sendNotificationToUser(notificationRequest);
    }

    @GetMapping("/user/{id}")
    public Page<NotificationDTO> getNotificationByUserId(@PathVariable(name = "id") Integer userId, Pageable pageable) {
        return service.getByUserId(userId, pageable);
    }


    @PutMapping("/update-read/{id}")
    public ResponseEntity<String> updateReadNotificationById(@PathVariable(name = "id") Integer id) {
        return this.service.updateReadById(id);
    }

    @GetMapping("/unread-count/{userId}")
    public Long countUnreadByUserId(@PathVariable(name = "userId") Integer userId) {
        return this.service.countUnreadByUserId(userId);
    }
}
