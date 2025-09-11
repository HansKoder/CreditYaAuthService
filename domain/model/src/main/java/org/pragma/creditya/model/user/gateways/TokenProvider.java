package org.pragma.creditya.model.user.gateways;

import org.pragma.creditya.model.user.User;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TokenProvider {
    Mono<String> generateToken (User user);
    Mono<Boolean> validateToken (String token);
}
