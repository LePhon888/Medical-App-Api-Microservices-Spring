package com.med.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.med.config.JwtHelper;
import com.med.dto.UserResponse;
import com.med.model.RefreshToken;
import com.med.model.User;
import com.med.service.RefreshTokenService;
import com.med.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private RefreshTokenService refreshTokenService;


    @Autowired
    private UserService userService;


    @Autowired
    private JwtHelper jwtHelper;


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> params) {
        Boolean authResult = this.userService.authUser(
                params.get("email"),
                params.get("password"));
        User user = this.userService.getUserByEmail(params.get("email"));
        Boolean enable = user.isEnabled();
        if (authResult) {
            if (enable) {
                Map<String, String> tokens = new HashMap<>();
                String accessToken = this.jwtHelper.generateToken(params.get("email"));
                RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(params.get("email"));
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken.getToken());
                return new ResponseEntity<>(tokens, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> params) {
        Map<String, String> tokens = new HashMap<>();
        return refreshTokenService.findByToken(params.get("refreshToken"))
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(userInfo -> {
                    String accessToken = this.jwtHelper.generateToken(params.get("email"));
                    tokens.put("accessToken", accessToken);
                    tokens.put("refreshToken", params.get("refreshToken"));
                    return new ResponseEntity<>(tokens, HttpStatus.OK);
                }).orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));
    }


    @PostMapping("/register")
    public ResponseEntity addUser(@RequestBody Map<String, String> params, HttpServletRequest request) throws MessagingException, JsonProcessingException {
        this.userService.register(params,getSiteURL(request));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity verifyUser(@RequestBody Map<String, String> params) {
        if (params != null && userService.verify(params)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/re-send-verify")
    public ResponseEntity reSendVerify(@RequestParam("email") String email) throws MessagingException, JsonProcessingException {
        User user = this.userService.getUserByEmail(email);
<<<<<<< HEAD
        System.out.println("emailemailemail " + email);
=======
>>>>>>> 1f67777 (add filter gateway)
        if (user != null && !user.isEnabled()) {
            this.userService.reSendVerify(email);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
        UserResponse u = this.userService.getUserRsponseByEmail(principal.getName());
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        try {
            if (jwtHelper.validateToken(token)) {
                return new ResponseEntity<>("Token is valid", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            System.out.println("Error while validating token: " + e.getMessage());
            return new ResponseEntity<>("An error occurred while validating the token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

}
