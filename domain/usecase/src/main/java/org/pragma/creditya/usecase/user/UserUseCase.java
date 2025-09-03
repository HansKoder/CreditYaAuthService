package org.pragma.creditya.usecase.user;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.model.user.exception.UsernameIsNotAvailableDomainException;
import org.pragma.creditya.model.user.gateways.UserRepository;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;
import org.pragma.creditya.usecase.user.ports.in.IUserUseCase;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {

    private final UserRepository userRepository;

    @Override
    public Mono<User> createUser(CreateUserCommand command) {
        return Mono.fromCallable(() -> checkUser(command))
                .flatMap(this::checkUsernameIsAvailable)
                .flatMap(userRepository::save);
    }

    private Mono<User> checkUsernameIsAvailable (User user) {
        return userRepository.existUsername(user.getUserName().getValue())
                .flatMap(exist -> {
                    if (exist) {
                        String err = String.format("Username %s is not available", user.getUserName().getValue());
                        return Mono.error(new UsernameIsNotAvailableDomainException(err));
                    }

                    return Mono.just(user);
                });

    }

    private User checkUser (CreateUserCommand command) {
        return User.create(command.username(), command.password());
    }
}
