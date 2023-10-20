package com.med.controller;

import com.cloudinary.Cloudinary;
import com.med.model.User;
import com.med.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RequestMapping("/api/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<String> createOrUpdate(@RequestPart(value = "user", required = false) User user,
                                                 @RequestPart(value = "file",required = false) MultipartFile file) {
       ResponseEntity response = userService.isUserValidation(user);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            if (file != null && !file.isEmpty())
                user.setFile(file);
            boolean updated = userService.update(user);
            if (updated) {
                return new ResponseEntity<>("User updated", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found or update failed", HttpStatus.NOT_FOUND);
            }
        } else {
            return response;
        }
    }

    @GetMapping("/")
    public ResponseEntity<Page<User>> getUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> entities = userService.getUsers(pageable);
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserDetail(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Integer id) {
        boolean deleteSuccessful = userService.delete(id);
        if (deleteSuccessful)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
