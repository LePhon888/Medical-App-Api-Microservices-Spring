package com.med.firebase.controller;

import com.med.firebase.model.UserDevice;
import com.med.firebase.service.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
