package com.med.controller;

import com.azure.core.annotation.Get;
import com.cloudinary.Cloudinary;
import com.med.dto.UserDTO;
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

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@CrossOrigin
@RequestMapping("/api/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/")

    public ResponseEntity<String> createOrUpdate(@RequestPart(value = "user", required = false) User user, @RequestPart(value = "file",required = false) MultipartFile file) {
       ResponseEntity response = this.isUserValidation(user);
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

    @GetMapping("/email/{email}")
    public CompletableFuture<ResponseEntity<String>> sendEmailOTP(@PathVariable(name = "email") String email) {
        return userService.sendEmailOTP(email);
    }

    @PostMapping("/email")
    public ResponseEntity<String> updateByEmail(@RequestBody Map<String, String> payload) {
        userService.updateUserPasswordByEmail(payload.get("email"), payload.get("password"));
        return ResponseEntity.ok("Update password successfully!");
    }

    public ResponseEntity isUserValidation(User user) {
        List<String> validRoles = Arrays.asList("ROLE_DOCTOR", "ROLE_PATIENT");
        if (user == null)
            return new ResponseEntity<>("Người dùng không hợp lệ", HttpStatus.BAD_REQUEST);
        else if (user.getEmail() == null || user.getEmail().isEmpty())
            return new ResponseEntity("Email là bắt buộc.", HttpStatus.BAD_REQUEST);
        else if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            return new ResponseEntity("Email không đúng định dạng.", HttpStatus.BAD_REQUEST);
        else if (user.getFirstName() == null || user.getFirstName().isEmpty())
            return new ResponseEntity("Họ tên không được để trống.", HttpStatus.BAD_REQUEST);
        else if (user.getBirthday() == null)
            return new ResponseEntity("Ngày sinh là bắt buộc.", HttpStatus.BAD_REQUEST);
        else if (user.getLastName() == null || user.getLastName().isEmpty())
            return new ResponseEntity("Tên không được để trống.", HttpStatus.BAD_REQUEST);
        else if (user.getAddress() == null || user.getAddress().isEmpty())
            return new ResponseEntity("Địa chỉ là bắt buộc.", HttpStatus.BAD_REQUEST);
        else if (user.getPassword() == null || user.getPassword().isEmpty())
            return new ResponseEntity("Mật khẩu là bắt buộc.", HttpStatus.BAD_REQUEST);
        else if (user.getUserRole() == null || user.getUserRole().isEmpty())
            return new ResponseEntity("Vai trò người dùng là bắt buộc.", HttpStatus.BAD_REQUEST);
        else if (!validRoles.contains(user.getUserRole()))
            return new ResponseEntity("Vai trò người dùng phải là ROLE_DOCTOR hoặc ROLE_PATIENT.", HttpStatus.BAD_REQUEST);
        else {
            LocalDate currentDate = LocalDate.now();
            LocalDate birthdate = user.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int age = Period.between(birthdate, currentDate).getYears();

            if (age < 18 || age > 60) {
                return new ResponseEntity("Tuổi phải từ 18 đến 60.", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/parent/{id}")
    public ResponseEntity<List<UserDTO>> getParent(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(userService.findByParentId(id), HttpStatus.OK);
    }

    @PostMapping("/parent")
    public ResponseEntity<User> createChildren(@RequestBody Map<String, String> user) {
        System.out.println("useruseruseruseruser " + user);
        User u = User.builder()
                .firstName(user.get("firstName"))
                .lastName(user.get("lastName"))
                .birthday(java.sql.Date.valueOf(user.get("birthday")))
                .userRole("ROLE_PATIENT")
                .isActive((short) 1)
                .parent(userService.getById(Integer.parseInt(user.get("parentId"))))
                .build();
        return new ResponseEntity<>(userService.create(u), HttpStatus.OK);
    }
}
