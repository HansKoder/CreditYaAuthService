package org.pragma.creditya.usecase.user.ports.in;

import org.pragma.creditya.model.user.User;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;
import reactor.core.publisher.Mono;

public interface IUserUseCase {

    Mono<User> createUser (CreateUserCommand command);

}
