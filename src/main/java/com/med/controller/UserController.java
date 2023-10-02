package com.med.controller;

import com.cloudinary.Cloudinary;
import com.med.model.User;
import com.med.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value =  "/{id}")
    public ResponseEntity<String> update(@RequestPart(value = "user", required = false) User user, @RequestPart(value = "file",required = false) MultipartFile file) {
        if (!file.isEmpty())
            user.setFile(file);
        boolean updated = userService.update(user);
        if (updated) {
            return new ResponseEntity<>("User updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found or update failed", HttpStatus.NOT_FOUND);
        }
    }
}
