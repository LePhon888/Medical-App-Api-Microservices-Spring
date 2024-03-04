package com.med.firebase.controller;

import com.med.firebase.dto.NotificationDTO;
import com.med.firebase.dto.NotificationRequest;
import com.med.firebase.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private NotificationService service;

    @PostMapping("/create")
    public ResponseEntity<String> createNotification(@RequestBody NotificationRequest request) {
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


    @PutMapping("/{id}")
    public ResponseEntity<String> updateReadNotificationById(@PathVariable(name = "id") Integer id) {
        return this.service.updateReadById(id);
    }

    @GetMapping("/unread-count/{userId}")
    public Long countUnreadByUserId(@PathVariable(name = "userId") Integer userId) {
        return this.service.countUnreadByUserId(userId);
    }
}
