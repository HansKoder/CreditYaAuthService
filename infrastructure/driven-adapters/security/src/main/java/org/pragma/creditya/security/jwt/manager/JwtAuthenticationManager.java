package org.pragma.creditya.security.jwt.manager;


import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.security.jwt.provider.JwtProvider;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .map(auth -> jwtProvider.getClaims(auth.getCredentials().toString()))
                // .onErrorResume(e -> Mono.error(new Throwable("bad token")))
                .map(this::setupToken);
    }

    private UsernamePasswordAuthenticationToken setupToken (Claims claims) {
        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                Stream.of(claims.get("roles"))
                        .map(role -> (List<Map<String, String>>) role)
                        .flatMap(role -> role.stream()
                                .map(r -> r.get("authority"))
                                .map(SimpleGrantedAuthority::new))
                        .toList());
    }

}