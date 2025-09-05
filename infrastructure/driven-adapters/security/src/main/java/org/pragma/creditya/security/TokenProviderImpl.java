package org.pragma.creditya.security;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.model.user.gateways.TokenProvider;
import org.pragma.creditya.security.jwt.provider.JwtProvider;
import org.pragma.creditya.security.mapper.SecurityMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TokenProviderImpl implements TokenProvider {

    private final JwtProvider jwtProvider;

    @Override
    public Mono<String> generateToken(User user) {
        return Mono.fromCallable(() -> jwtProvider.generateToken(SecurityMapper.toUserDetail(user)));
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> jwtProvider.validate(token));
    }
}
