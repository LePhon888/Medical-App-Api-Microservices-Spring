package com.med.notificationservice.controller;

import com.med.notificationservice.model.UserDevice;
import com.med.notificationservice.service.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-device")
public class UserDeviceController {

    @Autowired
    private UserDeviceService service;

    @PostMapping("/create")
    public ResponseEntity<String> createOrUpdateUserDevice(@RequestBody UserDevice userDevice) {
        return service.createOrUpdateUserDevice(userDevice);
    }

    @GetMapping("/user/{id}")
    public UserDevice getUserDeviceByUserId(@PathVariable(name = "id") Integer userId) {
        return service.getUserDeviceByUserId(userId);
    }

    @DeleteMapping("/delete/user/{id}")
    public ResponseEntity<String> deleteUserDeviceByUserId(@PathVariable(name = "id") Integer userId) {
        // Find the UserDevice by userId
        UserDevice userDevice = service.getUserDeviceByUserId(userId);

        // Check if the UserDevice exists
        if (userDevice != null) {
            // Delete the UserDevice
            service.deleteUserDeviceByUserId(userId);

            // Return a ResponseEntity with a success message
            return ResponseEntity.ok("UserDevice deleted successfully");
        } else {
            // If the UserDevice doesn't exist, return a ResponseEntity with an error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserDevice not found");
        }
    }


}
