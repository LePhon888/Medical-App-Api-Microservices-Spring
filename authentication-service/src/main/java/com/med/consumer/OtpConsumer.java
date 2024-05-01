package com.med.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OtpConsumer {

    @Autowired
    private OtpService otpService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "otp-send", groupId = "otp")
    public void sendOtpToEmail(String email) throws JsonProcessingException {
        // Generate OTP code for the provided email
        Integer code = otpService.createOtp(email);

        // Create a map containing the email and OTP code
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("code", code);

        // Convert the map to JSON string
        String message = objectMapper.writeValueAsString(map);

        // Send the OTP message to the "otp-email" Kafka topic
        kafkaTemplate.send("otp-email", message);
    }
}
