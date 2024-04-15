package com.med.gatewayservice.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/api/appointment"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints.stream().anyMatch(uri -> request.getURI().getPath().contains(uri));

}
