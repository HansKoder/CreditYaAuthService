package org.pragma.creditya.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.pragma.creditya.model.role.Role;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.usecase.role.IRoleUseCase;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;
import org.pragma.creditya.usecase.user.command.LoginCommand;
import org.pragma.creditya.usecase.user.ports.in.ILoginUseCase;
import org.pragma.creditya.usecase.user.ports.in.IUserUseCase;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class AuthApplicationServiceTest {

    @MockitoBean
    private IUserUseCase userUseCase;

    @MockitoBean
    private IRoleUseCase roleUseCase;

    @MockitoBean
    private ILoginUseCase loginUseCase;

    @InjectMocks
    private AuthApplicationService authApplicationService;

    private final String EXAMPLE_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";

    private final User USER_EXAMPLE = User.Builder.anUser()
            .userName("doe@gmail.com")
            .password("xxx")
            .build();


    private final Role ROLE_EXAMPLE = Role.RoleBuilder.aRole()
            .id(1L)
            .name("Customer")
            .build();


    @BeforeEach
    void setup () {
        userUseCase = Mockito.mock(IUserUseCase.class);
        roleUseCase = Mockito.mock(IRoleUseCase.class);
        loginUseCase = Mockito.mock(ILoginUseCase.class);

        authApplicationService = new AuthApplicationService(userUseCase, roleUseCase, loginUseCase);
    }

    @Test
    void shouldBeCreatedUserWithSuccessful () {
        CreateUserCommand command = new CreateUserCommand("doe@gmail.com", "123", 1L);

        Mockito.when(userUseCase.checkInitializationUer(command))
                .thenReturn(Mono.just(USER_EXAMPLE));

        Mockito.when(userUseCase.checkUsernameIsAvailable(USER_EXAMPLE))
                .thenReturn(Mono.just(USER_EXAMPLE));

        Mockito.when(userUseCase.persist(USER_EXAMPLE))
                .thenReturn(Mono.just(USER_EXAMPLE));

        Mockito.when(roleUseCase.checkRole(1L))
                .thenReturn(Mono.just(ROLE_EXAMPLE));

        User expected = User.Builder.anUser()
                .userName(USER_EXAMPLE.getUserName().getValue())
                .password(USER_EXAMPLE.getPassword().value())
                .roleId(command.roleId())
                .build();

        var resp = authApplicationService.createUser(command);
        StepVerifier.create(resp)
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void shouldBeAuthenticatedWithSuccessful () {
        LoginCommand command = new LoginCommand("doe@gmail.com", "123");

        Mockito.when(loginUseCase.handler(command))
                .thenReturn(Mono.just(EXAMPLE_JWT));

        var resp = authApplicationService.login(command);
        StepVerifier.create(resp)
                .expectNext(EXAMPLE_JWT)
                .verifyComplete();
    }

}
