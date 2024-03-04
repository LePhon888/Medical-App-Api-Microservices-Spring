package com.med.controller;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.med.model.User;
import com.med.security.JwtHelper;
import com.med.service.GoogleSignInService;
import com.med.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/login-google")
public class GoogleSignInController {

    @Autowired
    protected GoogleSignInService googleSignInService;

    @Autowired
    private JwtHelper jwtHelper;
    @PostMapping("/auth-google")
    public ResponseEntity<String> verifyGoogleLogin(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> user = (Map<String, Object>) requestBody.get("user");

        if (this.googleSignInService.verify(requestBody)) {
            String token = this.jwtHelper.generateToken((String) user.get("email"));
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Error", HttpStatus.UNAUTHORIZED);
    }
}
