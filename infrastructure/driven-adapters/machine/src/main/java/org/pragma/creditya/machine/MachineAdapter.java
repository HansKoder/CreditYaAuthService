package org.pragma.creditya.machine;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.machine.config.MachineConfigProperties;
import org.pragma.creditya.usecase.user.ports.in.MachineRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class MachineAdapter implements MachineRepository {

    private final MachineConfigProperties properties;

    @Override
    public Mono<String> authenticationMachine(String clientId, String clientSecret) {
        if (checkCredentials(clientId, clientSecret))
            return Mono.empty();

        if (!isAuthenticated(clientId, clientSecret))
            return Mono.empty();

        return Mono.just(clientId);
    }

    private Boolean isAuthenticated (String id, String secret) {
        return id.equals(properties.id()) &&
                secret.equals(properties.secret());
    }

    private Boolean checkCredentials (String id, String secret) {
        return id == null || id.isBlank() ||
                secret == null || secret.isBlank();
    }

}
