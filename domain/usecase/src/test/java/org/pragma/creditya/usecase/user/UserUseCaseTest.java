package org.pragma.creditya.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.model.user.exception.UserDomainException;
import org.pragma.creditya.model.user.exception.UsernameIsNotAllowedDomainException;
import org.pragma.creditya.model.user.gateways.EncodeProvider;
import org.pragma.creditya.model.user.gateways.UserRepository;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserUseCaseTest {

    @InjectMocks
    private UserUseCase userUseCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncodeProvider encodeProvider;

    private final User USER_EXAMPLE = User.Builder.anUser()
            .userName("doe@gmail.com")
            .password("xxx")
            .build();

    @BeforeEach
    void setup () {
        userRepository = Mockito.mock(UserRepository.class);
        encodeProvider = Mockito.mock(EncodeProvider.class);

        userUseCase = new UserUseCase(userRepository, encodeProvider);
    }

    @Test
    void shouldThrowExceptionWhenUserNameIsNull () {
        when(encodeProvider.encode(anyString()))
                .thenReturn("");

        CreateUserCommand cmd = new CreateUserCommand(null, "123456", null);
        StepVerifier.create(userUseCase.checkInitializationUer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Username must be mandatory", throwable.getMessage());
                    assertInstanceOf(UserDomainException.class, throwable);
                }).verify();

        verify(encodeProvider, Mockito.times(1)).encode(anyString());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsEmpty () {
        when(encodeProvider.encode(anyString()))
                .thenReturn("");

        CreateUserCommand cmd = new CreateUserCommand("doe@gmail.com", " ", null);
        StepVerifier.create(userUseCase.checkInitializationUer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Password must be mandatory", throwable.getMessage());
                    assertInstanceOf(UserDomainException.class, throwable);
                }).verify();
    }

    @Test
    void shouldBeInitializedWithSuccess_BecauseUserCredentialsAreValid () {
        when(encodeProvider.encode(anyString()))
                .thenReturn("****");

        CreateUserCommand cmd = new CreateUserCommand("doe@gmail.com", "123", null);
        var response = userUseCase.checkInitializationUer(cmd);

        StepVerifier.create(response)
                .expectNextMatches(user -> user.getPassword().value().equals("****")
                ).verifyComplete();
    }

    @Test
    @DisplayName(value = "Should Throw Exception UsernameIsNotAvailableDomainException when try to persist another record with the same username")
    void shouldThrowExceptionWhenUsernameIsNotAvailable () {
        when(userRepository.existUsername("doe@gmail.com"))
                .thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(userUseCase.checkUsernameIsAvailable(USER_EXAMPLE))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Username doe@gmail.com is not available", throwable.getMessage());
                    assertInstanceOf(UsernameIsNotAllowedDomainException.class, throwable);
                }).verify();

        verify(userRepository, Mockito.times(1)).existUsername(anyString());
    }

    @Test
    @DisplayName(value = "Should be unique username, then is enabled to be used")
    void shouldBeUniqueUsername () {
        when(userRepository.existUsername("doe@gmail.com"))
                .thenReturn(Mono.just(Boolean.FALSE));

        var response = userUseCase.checkUsernameIsAvailable(USER_EXAMPLE);
        StepVerifier.create(response)
                .expectNextMatches(
                        u -> u.getUserName().getValue().equals("doe@gmail.com")
                ).verifyComplete();


        verify(userRepository, Mockito.times(1)).existUsername(anyString());
    }


    @Test
    void shouldThrowExceptionWhenDBIsNotWorking () {
        when(userRepository.save(any(User.class)))
                .thenReturn(Mono.error(new RuntimeException("DB is not working")));

        StepVerifier.create(userUseCase.persist(USER_EXAMPLE))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("DB is not working", throwable.getMessage());
                    assertInstanceOf(Exception.class, throwable);
                }).verify();

        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }


    @Test
    void shouldBePersistedUserWithSuccessful () {
        when(userRepository.save(USER_EXAMPLE))
                .thenReturn(Mono.just(USER_EXAMPLE));

        var resp = userUseCase.persist(USER_EXAMPLE);
        StepVerifier.create(resp)
                .expectNext(USER_EXAMPLE)
                .verifyComplete();

        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

}
