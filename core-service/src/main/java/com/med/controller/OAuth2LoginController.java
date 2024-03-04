package com.med.controller;//package com.med.controller;
//
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.core.OAuth2RefreshToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Controller
//public class OAuth2LoginController {
//    @GetMapping("/login/oauth2/code/google")
//    public String loginGoogleCallback(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
//        // You can access the user's information from oauth2User.
//        // Here, you can retrieve the access token if needed.
//`
//         String accessToken = oauth2User.getAccessToken().getTokenValue();
//
//        return "redirect:/"; // Redirect to your desired page after successful login.
//    }
//}
