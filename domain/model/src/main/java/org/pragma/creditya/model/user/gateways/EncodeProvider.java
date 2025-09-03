package org.pragma.creditya.model.user.gateways;

import reactor.core.publisher.Mono;

public interface EncodeProvider {
    Mono<String> encode (String pass);
    Mono<Boolean> matches (String inputPassword, String hashedPassword);
}
