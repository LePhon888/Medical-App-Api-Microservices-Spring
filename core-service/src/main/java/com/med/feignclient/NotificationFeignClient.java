package com.med.feignclient;

import com.med.dto.NotificationRequestDTO;
import com.med.dto.UserDeviceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "notification-service")
public interface NotificationFeignClient {

    @GetMapping("/api/user-device/user/{id}")
    UserDeviceDTO getUserDeviceByUserId(@RequestParam(name = "id") Integer userId);

    @PostMapping("/api/notification/send")
    ResponseEntity<String> sendPushNotification(@RequestBody NotificationRequestDTO notificationRequest);

}
