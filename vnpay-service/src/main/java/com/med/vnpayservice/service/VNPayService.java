package com.med.vnpayservice.service;

import com.netflix.discovery.converters.Auto; // Assuming this import is not needed
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class VNPayService {
    private final WebClient.Builder webClientBuilder;

    public Mono<String> updatePaidAppointment(String orderInfo, String paymentTime) {
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("id", orderInfo);
        pathVariables.put("date", paymentTime);

        return webClientBuilder.build().put()
                .uri("http://core-service/api/appointment/update-paid", uriBuilder -> uriBuilder
                        .queryParam("id", orderInfo)
                        .queryParam("date", paymentTime)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> System.out.println("Response: " + response))
                .doOnError(error -> System.err.println("Error: " + error));
    }
}