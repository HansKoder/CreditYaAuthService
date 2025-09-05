package org.pragma.creditya.usecase.user.ports.in;

import org.pragma.creditya.usecase.user.command.LoginCommand;
import reactor.core.publisher.Mono;

public interface ILoginUseCase {

    Mono<String> handler(LoginCommand command);

}
