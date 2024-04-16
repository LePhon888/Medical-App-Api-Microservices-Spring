package com.med.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.med.config.JwtHelper;
import com.med.model.RefreshToken;
import com.med.service.GoogleSignInService;
import com.med.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login-google")
public class GoogleController {

    @Autowired
    protected GoogleSignInService googleSignInService;

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @PostMapping("/auth-google")
    public ResponseEntity<Map<String, String>> verifyGoogleLogin(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> user = (Map<String, Object>) requestBody.get("user");

        if (this.googleSignInService.verify(requestBody)) {
            String token = this.jwtHelper.generateToken((String) user.get("email"));
            RefreshToken refreshToken = this.refreshTokenService.createRefreshToken((String) requestBody.get("email"));
            java.sql.Timestamp expiryDateAccessToken = new java.sql.Timestamp(System.currentTimeMillis() + JwtHelper.JWT_TOKEN_VALIDITY * 1000);


            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", token);
            tokens.put("refreshToken", refreshToken.getToken());
            tokens.put("expiredDateRefreshToken", String.valueOf(refreshToken.getExpiryDate()));
            tokens.put("expiredDateAccessToken", String.valueOf(expiryDateAccessToken));
            return new ResponseEntity<>(tokens, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
