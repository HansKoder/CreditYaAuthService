package org.pragma.creditya.model.user.gateways;

import org.pragma.creditya.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> save(User domain);

}
