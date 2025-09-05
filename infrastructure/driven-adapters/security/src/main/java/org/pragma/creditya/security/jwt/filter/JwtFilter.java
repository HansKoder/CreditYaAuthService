package org.pragma.creditya.security.jwt.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class JwtFilter implements WebFilter {

    private Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        if(path.contains("api/v1/login"))
            return chain.filter(exchange);

        String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(auth == null || !auth.startsWith("Bearer ")) {
            logger.error("[infra.security] check token, token is missing or invalid");
            return this.handleError(exchange, new RuntimeException("Missing or invalid token"));
        }

        String token = auth.replace("Bearer ", "");
        exchange.getAttributes().put("token", token);

        return chain.filter(exchange)
                .onErrorResume(e -> this.handleError(exchange, e));
    }

    private Mono<Void> handleError(ServerWebExchange exchange, Throwable ex) {
        logger.error("[infra.security] (jwt-filter) error with jwt filter ", ex);

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"error\": \"%s\"}", ex.getMessage());
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }
}
