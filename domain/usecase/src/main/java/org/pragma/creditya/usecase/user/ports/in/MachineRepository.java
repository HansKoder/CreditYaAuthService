package org.pragma.creditya.usecase.user.ports.in;

import reactor.core.publisher.Mono;

public interface MachineRepository {

    Mono<String> authenticationMachine (String clientId, String clientSecret);

}
