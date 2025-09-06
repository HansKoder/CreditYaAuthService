package org.pragma.creditya.usecase.user;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.model.user.exception.UsernameIsNotAllowedDomainException;
import org.pragma.creditya.model.user.gateways.EncodeProvider;
import org.pragma.creditya.model.user.gateways.UserRepository;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;
import org.pragma.creditya.usecase.user.ports.in.IUserUseCase;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {

    private final UserRepository userRepository;
    private final EncodeProvider encodeProvider;

    // ignore for now, this need to be removed this method.
    /*
    @Override
    public Mono<User> createUser(CreateUserCommand command) {
        return Mono.fromCallable(() -> checkUser(command))
                .flatMap(this::checkUsernameIsAvailable)
                .flatMap(userRepository::save);
    }
     */

    @Override
    public Mono<User> checkUsernameIsAvailable (User user) {
        return userRepository.existUsername(user.getUserName().getValue())
                .flatMap(exist -> {
                    if (!exist) return Mono.just(user);

                    String err = String.format("Username %s is not available", user.getUserName().getValue());
                    return Mono.error(new UsernameIsNotAllowedDomainException(err));
                });
    }

    @Override
    public Mono<User> checkInitializationUer(CreateUserCommand command) {
        return Mono.fromCallable(() -> checkUser(command));
    }

    @Override
    public Mono<User> persist(User user) {
        return userRepository.save(user);
    }

    private User checkUser (CreateUserCommand command) {
        User user = User.createUser(command.username(), encodeProvider.encode(command.password()));
        user.checkCreationUser();
        return user;
    }

}
