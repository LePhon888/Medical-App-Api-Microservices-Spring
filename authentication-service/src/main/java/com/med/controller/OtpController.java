package com.med.controller;
import com.med.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/otp")
public class OtpController {
    @Autowired
    private OtpService otpService;

    @GetMapping("validate")
    public ResponseEntity<String> checkValidateOtp (@RequestParam Map<String, String> params) {
        return this.otpService.checkValidOtp(params.get("email"), Integer.valueOf(params.get("code")));
    }
}
