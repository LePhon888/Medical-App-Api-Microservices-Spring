package com.med.notificationservice.service;

import com.med.notificationservice.model.UserDevice;
import com.med.notificationservice.repository.UserDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserDeviceService {

    @Autowired
    private UserDeviceRepository repository;

    public ResponseEntity<String> createOrUpdateUserDevice(UserDevice incomingUserDevice) {
        Optional<UserDevice> existingUserDevice = repository.findById(incomingUserDevice.getId());

        if (existingUserDevice.isPresent()) {
            UserDevice userDevice = existingUserDevice.get();
            userDevice.setTokenRegistration(incomingUserDevice.getTokenRegistration());
            repository.save(userDevice);
            return ResponseEntity.ok("UserDevice updated successfully");
        } else {
            UserDevice newUserDevice = new UserDevice(
                    0,
                    incomingUserDevice.getUserId(),
                    incomingUserDevice.getTokenRegistration(),
                    LocalDateTime.now()
            );
            repository.save(newUserDevice);
            return ResponseEntity.status(HttpStatus.CREATED).body("UserDevice created successfully");
        }
    }

    public UserDevice getUserDeviceByUserId (Integer userId) {
        return repository.findUserDeviceByUserId(userId);
    }
}
