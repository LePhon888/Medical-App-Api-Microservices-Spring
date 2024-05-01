package com.med.service;

import com.med.model.Otp;
import com.med.repository.OtpRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {
    @Autowired
    private OtpRepository repository;

    @Transactional
    public Integer createOtp (String email) {
        repository.deleteAllOtpByEmail(email);
        Integer code = Integer.valueOf(new DecimalFormat("000000").format(new Random().nextInt(900000) + 100000));
        repository.save(new Otp(
                0,
                code,
                email,
                LocalDateTime.now().plusMinutes(10)));
        return code;
    }

    public ResponseEntity<String> checkValidOtp(String email, Integer code) {
        Optional<Otp> checkOtp = repository.findValidOtpByEmailAndCode(email, code, LocalDateTime.now());
        if (checkOtp.isPresent()) {
            return ResponseEntity.ok("Valid OTP");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid OTP");
        }
    }
}
