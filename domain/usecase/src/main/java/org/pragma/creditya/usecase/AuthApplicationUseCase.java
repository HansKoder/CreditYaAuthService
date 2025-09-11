package org.pragma.creditya.usecase;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.role.Role;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.usecase.role.IRoleUseCase;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;
import org.pragma.creditya.usecase.user.command.LoginCommand;
import org.pragma.creditya.usecase.user.ports.in.ILoginUseCase;
import org.pragma.creditya.usecase.user.ports.in.IUserUseCase;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RequiredArgsConstructor
public class AuthApplicationUseCase implements IAuthApplicationUseCase {

    private final IUserUseCase userUseCase;
    private final IRoleUseCase roleUseCase;
    private final ILoginUseCase loginUseCase;

    @Override
    public Mono<User> createUser(CreateUserCommand command) {
        return userUseCase.checkInitializationUer(command)
                .zipWith(roleUseCase.checkRole(command.roleId()))
                .map(this::assignRole)
                .flatMap(userUseCase::checkUsernameIsAvailable)
                .flatMap(userUseCase::persist);

    }

    private User assignRole (Tuple2<User, Role> tuple) {
        User user = tuple.getT1();
        Role role = tuple.getT2();
        user.assignRole(role.getId());
        return user;
    }

    @Override
    public Mono<String> login(LoginCommand command) {
        return loginUseCase.handler(command);
    }
}
