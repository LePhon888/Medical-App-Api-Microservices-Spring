package com.med.config;

import com.med.oauth2.CustomOAuth2User;
import com.med.oauth2.CustomOAuth2UserService;
import com.med.oauth2.OAuth2LoginSuccessHandler;
import com.med.security.JwtAuthenticationEntryPoint;
import com.med.security.JwtAuthenticationFilter;

import com.med.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.io.IOException;
import java.text.ParseException;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomOAuth2UserService oauthUserService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/openai-api-endpoint").permitAll()
                                .requestMatchers("/api/payment").permitAll()
                                .requestMatchers("/oauth2/authorization/google").permitAll()
                                .requestMatchers("/chat-medical").permitAll()
                                .requestMatchers("/auth/login",
                                        "/auth/register",
                                        "/",
                                        "/auth/verify",
                                        "/api/hours",
                                        "/api/departments",
                                        "/api/scrape",
                                        "/api/doctors/department",
                                        "/api/doctors/**",
                                        "/api/user/**",
                                        "/auth/current-user",
                                        "/api/rating/**",
                                        "/api/doctor-details/**"
                                        ).permitAll()
                                .requestMatchers("/test", "/api/appointment").access("hasRole('ROLE_PATIENT') or hasRole('ROLE_DOCTOR')")
//                                .requestMatchers("/auth/current-user").access("hasRole('ROLE_PATIENT') " +
//                                        "or hasRole('ROLE_DOCTOR') or hasRole('ROLE_ADMIN')")

                )
                .oauth2Login(oauth2 -> {
                    oauth2.userInfoEndpoint(userInfo ->
                            userInfo.userService(oauthUserService)
                    ).successHandler((request, response, authentication) -> {
                        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
                        try {
                            userService.processOAuthPostLogin(oidcUser);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        response.sendRedirect("/");
                    });
                })
                .formLogin(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point));
        return http.build();
    }
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
}
