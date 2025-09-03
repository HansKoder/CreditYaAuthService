package org.pragma.creditya.model.user.gateways;

import org.pragma.creditya.model.user.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository {
    Mono<User> save(User domain);
    Mono<Boolean> existUsername (String username);
    Mono<User> findByUsername (String username);
}
