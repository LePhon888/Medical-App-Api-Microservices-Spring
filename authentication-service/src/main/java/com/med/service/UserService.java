package com.med.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.dto.UserResponse;
import com.med.model.User;
import com.med.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public User getUserByEmail(String email) {
        return this.userRepository.getUserByEmail(email);
    }

    public UserResponse getUserRsponseByEmail(String email) {
        return this.userRepository.getUserResponseByEmail(email);
    }


    public boolean authUser(String email, String password) {
        User u = this.userRepository.getUserByEmail(email);
        return passwordEncoder.matches(password, u.getPassword());
    }

    public void register(Map<String, String> params, String siteURL) throws JsonProcessingException {
        User u = new User();
        u.setEmail(params.get("email"));
        u.setFirstName(params.get("firstName"));
        u.setLastName(params.get("lastName"));
        u.setPassword(this.passwordEncoder.encode(params.get("password")));
        u.setUserRole("ROLE_PATIENT");
        u.setProvider("LOCAL");
        u.setIsActive((short) 1);

        Random random = new Random();
        int randomCode = 100000 + random.nextInt(900000);

        u.setCode(randomCode);
        u.setCreatedDateCode(new java.util.Date());
        u.setEnabled(false);
        User savedUser = this.userRepository.save(u);
        Map<String, String> user = new HashMap<>();
        user.put("email", u.getEmail());
        user.put("code", String.valueOf(randomCode));
        user.put("name", u.getLastName() + u.getFirstName());

        kafkaTemplate.send("verifyEmail", objectMapper.writeValueAsString(user));
    }

    public boolean verify(Map<String, String> params) {
        User user = this.userRepository.getUserByEmail(params.get("email"));

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            if (user.getCode() == Integer.parseInt(params.get("code")) && new Date().getTime() - user.getCreatedDateCode().getTime() <= 1800000) {
                user.setEnabled(true);
                this.userRepository.save(user);
                return true;
            } else {
                return false;
            }
        }
    }

    //re-send
    public void reSendVerify(String email) throws JsonProcessingException {
        User user = this.userRepository.getUserByEmail(email);
        if (user != null && !user.isEnabled()) {
<<<<<<< HEAD
            Random random = new Random();
            int randomCode = 100000 + random.nextInt(900000);
            user.setCode(randomCode);
            user.setCreatedDateCode(new java.util.Date());
            user = this.userRepository.save(user);
            Map<String, String> userMap = new HashMap<>();
            userMap.put("email", user.getEmail());
            userMap.put("code", String.valueOf(user.getCode()));
            userMap.put("name", user.getLastName() + " " + user.getFirstName());
=======
            Map<String, String> userMap = new HashMap<>();
            userMap.put("email", user.getEmail());
            userMap.put("code", String.valueOf(user.getCode()));
            userMap.put("name", user.getLastName() + user.getFirstName());
>>>>>>> 1f67777 (add filter gateway)
            kafkaTemplate.send("verifyEmail", objectMapper.writeValueAsString(userMap));
        }
    }

<<<<<<< HEAD
    public User create (User u) {
        return this.userRepository.save(u);
    }

=======
>>>>>>> 1f67777 (add filter gateway)
}
