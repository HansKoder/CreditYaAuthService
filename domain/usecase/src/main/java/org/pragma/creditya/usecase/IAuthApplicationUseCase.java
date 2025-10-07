package org.pragma.creditya.usecase;

import org.pragma.creditya.model.user.User;
import org.pragma.creditya.usecase.machine.command.AuthenticationMachineCommand;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;
import org.pragma.creditya.usecase.user.command.LoginCommand;
import reactor.core.publisher.Mono;

public interface IAuthApplicationUseCase {

     Mono<User> createUser(CreateUserCommand command);

    Mono<String> login(LoginCommand command);

    Mono<String> authenticationMachine (AuthenticationMachineCommand command);
}
