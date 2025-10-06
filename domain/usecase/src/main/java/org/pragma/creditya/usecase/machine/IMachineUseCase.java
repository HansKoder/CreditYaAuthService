package org.pragma.creditya.usecase.machine;

import org.pragma.creditya.usecase.machine.command.AuthenticationMachineCommand;
import reactor.core.publisher.Mono;

public interface IMachineUseCase {
    Mono<String> authentication(AuthenticationMachineCommand command);
}
