package com.med.gatewayservice.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    private RouteValidator validator;

    @Autowired
    private RestTemplate template;

    public AuthenticationFilter(WebClient.Builder webClientBuilder, WebClient.Builder webClientBuilder1) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder1;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // Check for authorization header
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    // Validate token synchronously
                    String finalAuthHeader = authHeader;
                    return webClientBuilder
                            .build().get()
                            .uri("http://authentication-service/api/auth/validate", uriBuilder -> uriBuilder
                                    .queryParam("token", finalAuthHeader).build())
                            .retrieve()
                            .onStatus(HttpStatusCode::is4xxClientError,
                                    response -> {
                                        System.out.println("Client Error: " + response.statusCode().value());
                                        return Mono.error(new RuntimeException("Unauthorized access"));
                                    }
                            )
                            .onStatus(HttpStatusCode::is5xxServerError,
                                    response -> {
                                        System.out.println("Server Error: " + response.statusCode().value());
                                        return Mono.error(new RuntimeException("Server error"));
                                    }
                            )
                            .onStatus(HttpStatusCode::is2xxSuccessful,
                                    response -> {
                                        System.out.println("Success: " + response.statusCode().value());
                                        return Mono.empty();
                                    }
                            )
                            .bodyToMono(String.class)
                            .flatMap(res -> {
                                // Handle successful response (status code 200)
                                System.out.println("Response: " + res);
                                return chain.filter(exchange);
                            });
                } catch (Exception e) {
                    System.out.println(e);
                    throw new RuntimeException("Unauthorized access: " + e.getMessage());
                }
            }
            return chain.filter(exchange);
        });
    }
    public static class Config {

    }
}