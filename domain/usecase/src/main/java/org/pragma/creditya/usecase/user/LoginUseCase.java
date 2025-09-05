package org.pragma.creditya.usecase.user;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.shared.exception.DomainException;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.model.user.exception.InvalidCredentialsDomainException;
import org.pragma.creditya.model.user.exception.UserLockedDomainException;
import org.pragma.creditya.model.user.gateways.EncodeProvider;
import org.pragma.creditya.model.user.gateways.TokenProvider;
import org.pragma.creditya.model.user.gateways.UserRepository;
import org.pragma.creditya.model.user.valueobject.UserName;
import org.pragma.creditya.usecase.user.command.LoginCommand;
import org.pragma.creditya.usecase.user.ports.in.ILoginUseCase;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class LoginUseCase implements ILoginUseCase {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final EncodeProvider encodeProvider;

    @Override
    public Mono<String> handler(LoginCommand command) {
        return Mono.fromCallable(() -> checkCredentials(command))
                .map(User::getUserName)
                .flatMap(this::checkUserName)
                .flatMap(this::checkShouldBeBlocked)
                .flatMap(u -> checkPass(u, command.password()))
                .flatMap(tokenProvider::generateToken);
    }

    private User checkCredentials (LoginCommand command) {
        return User.Builder.anUser()
                .userName(command.username())
                .password(command.password())
                .build();
    }

    private Mono<User> checkUserName (UserName userName) {
        return userRepository.findByUsername(userName.getValue())
                .switchIfEmpty(Mono.error(new InvalidCredentialsDomainException("Invalid Credentials")));
    }

    private Mono<User> checkUserIsLocked (User user) {
        try {
            user.checkIsLocked();
            return Mono.just(user);
        } catch (UserLockedDomainException ex) {
            return Mono.error(ex);
        }
    }

    private Mono<User> checkShouldBeBlocked(User user) {
        return checkUserIsLocked(user)
                .flatMap(u -> {
                    if (u.shouldBeBlocked()) {
                        return userRepository.save(u)
                                .flatMap(saved ->
                                        Mono.error(userLockedEx(saved)));
                    }
                    return Mono.just(u);
                });
    }

    private UserLockedDomainException userLockedEx (User user) {
        return new UserLockedDomainException("User " + user.getUserName().getValue() + " is Locked, invalid any operation until new order");
    }

    private Boolean isMatchPass (String passEncoded, String passInput) {
        return encodeProvider.matches(passInput, passEncoded);
    }

    private Mono<User> checkPass (User u, String p) {
        if (isMatchPass(u.getPassword().value(), p))
            return Mono.just(u);

        u.loginFailed();
        DomainException ex = u.shouldBeBlocked() ? userLockedEx(u) :
                new InvalidCredentialsDomainException("Invalid Credentials");

        return userRepository.save(u)
                .flatMap(saved ->
                        Mono.error(ex));
    }
}
