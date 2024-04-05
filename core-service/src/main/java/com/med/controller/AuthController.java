package com.med.controller;

import com.med.model.JWTRequest;
import com.med.model.JWTResponse;
import com.med.model.RefreshToken;
import com.med.model.User;
import com.med.security.JwtHelper;
import com.med.service.RefreshTokenService;
import com.med.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

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
    public ResponseEntity addUser(@RequestBody Map<String, String> params, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        this.userService.register(params,getSiteURL(request));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/verify")
    public ResponseEntity verifyUser(@Param("code") String code) {
        if (code != null && userService.verify(code)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        User u = this.userService.getUserByEmail(principal.getName());
        return new ResponseEntity<>(u, HttpStatus.OK);
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
