package org.pragma.creditya.model.user.gateways;

import reactor.core.publisher.Mono;

import java.util.List;

public interface TokenProvider {
    Mono<String> generateToken (String subject, List<String> roles);
    Mono<Boolean> validateToken (String token);
}
