package com.med.service;

import org.springframework.stereotype.Service;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.med.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class GoogleSignInService {

    @Value("${google.clientId}")
    private String clientId;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    public Boolean verify (Map<String, Object> requestBody) {
        String idToken = (String) requestBody.get("idToken");
        Map<String, Object> user = (Map<String, Object>) requestBody.get("user");

        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();

        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken != null) {
                String email = googleIdToken.getPayload().getEmail();
                User u = this.userService.getUserByEmail(email);
                if (u == null)
                {
                    User saveUser = new User();
                    saveUser.setImage((String) user.get("photo"));
                    if (requestBody.get("familyName") != null)
                        saveUser.setLastName((String) user.get("familyName"));
                    saveUser.setFirstName((String) user.get("name"));
                    saveUser.setEmail((String) user.get("email"));
                    saveUser.setPassword(this.passwordEncoder.encode((String) user.get("id")));
                    saveUser.setProvider("GOOGLE");
                    saveUser.setEnabled(true);
                    saveUser.setUserRole("ROLE_PATIENT");
                    saveUser.setIsActive((short) 1);
                    this.userService.create(saveUser);
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error verifying ID token.");
        }
        return false;
    }
}
