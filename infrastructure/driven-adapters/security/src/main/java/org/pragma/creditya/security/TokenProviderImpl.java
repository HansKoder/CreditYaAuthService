package org.pragma.creditya.security;

import org.pragma.creditya.model.user.User;
import org.pragma.creditya.model.user.gateways.TokenProvider;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class TokenProviderImpl implements TokenProvider {
    @Override
    public Mono<String> generateToken(User user) {
        return null;
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return null;
    }
}
