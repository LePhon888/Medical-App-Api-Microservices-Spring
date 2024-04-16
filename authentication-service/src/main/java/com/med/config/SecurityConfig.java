package com.med.config;

import com.med.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Autowired
    private JwtAuthenticationFilter filter;

//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorizeRequests ->
                                authorizeRequests
//                                        .requestMatchers("/openai-api-endpoint").permitAll()
                                        .requestMatchers("/api/payment").permitAll()
//                                        .requestMatchers("/oauth2/authorization/google").permitAll()
                                        .requestMatchers("/chat-medical").permitAll()
                                        .requestMatchers("/auth/login",
                                                "/auth/register",
                                                "/",
                                                "/auth/verify",
                                                "/api/hours",
                                                "/api/scrape",
                                                "/api/doctors/department",
                                                "/api/doctors/**",
                                                "/api/user/**",
                                                "/auth/current-user",
                                                "/api/rating/**",
                                                "/api/doctor-details/**",
                                                "/api/medication-schedule/**",
                                                "/api/medicine/**",
                                                "/api/medicine-unit/**",
                                                "/api/schedule-time/**",
                                                "/api/schedule-time-detail/**"
                                        ).permitAll()
                                        .requestMatchers("/api/appointment").access("hasRole('ROLE_PATIENT') or hasRole('ROLE_DOCTOR')")
                                        .requestMatchers("/api/departments").access("hasRole('ROLE_PATIENT') or hasRole('ROLE_DOCTOR')")
//                                .requestMatchers("/auth/current-user").access("hasRole('ROLE_PATIENT') " +
//                                        "or hasRole('ROLE_DOCTOR') or hasRole('ROLE_ADMIN')")

                )
//                .oauth2Login(oauth2 -> {
//                    oauth2.userInfoEndpoint(userInfo ->
//                            userInfo.userService(oauthUserService)
//                    ).successHandler((request, response, authentication) -> {
//                        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
//                        try {
//                            userService.processOAuthPostLogin(oidcUser);
//                        } catch (ParseException e) {
//                            throw new RuntimeException(e);
//                        }
//
//                        response.sendRedirect("/");
//                    });
//                })
//                .formLogin(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point));
        return http.build();
    }
}
