package org.pragma.creditya.usecase.machine;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.shared.exception.UnAuthorizeDomainException;
import org.pragma.creditya.model.user.gateways.TokenProvider;
import org.pragma.creditya.usecase.machine.command.AuthenticationMachineCommand;
import org.pragma.creditya.usecase.user.ports.in.MachineRepository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MachineUseCase implements IMachineUseCase{

    private final TokenProvider tokenProvider;
    private final MachineRepository machineRepository;

    @Override
    public Mono<String> authentication(AuthenticationMachineCommand command) {
        return machineRepository.authenticationMachine(command.clientId(), command.clientSecret())
                .flatMap(tokenProvider::generateMachineToken)
                .switchIfEmpty(Mono.error(new UnAuthorizeDomainException("Client " + command.clientId() + " is UnAuthorized")));
    }

}
