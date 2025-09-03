package org.pragma.creditya.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.model.user.exception.UserDomainException;
import org.pragma.creditya.model.user.exception.UsernameIsNotAvailableDomainException;
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

    @BeforeEach
    void setup () {
        userRepository = Mockito.mock(UserRepository.class);
        userUseCase = new UserUseCase(userRepository);
    }

    @Test
    void shouldThrowExceptionWhenUserNameIsNull () {
        CreateUserCommand cmd = new CreateUserCommand(null, "123456");
        StepVerifier.create(userUseCase.createUser(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Username must be mandatory", throwable.getMessage());
                    assertInstanceOf(UserDomainException.class, throwable);
                }).verify();
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsEmpty () {
        CreateUserCommand cmd = new CreateUserCommand("doe@gmail.com", " ");
        StepVerifier.create(userUseCase.createUser(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Password must be mandatory", throwable.getMessage());
                    assertInstanceOf(UserDomainException.class, throwable);
                }).verify();
    }

    @Test
    @DisplayName(value = "Should Throw Exception UsernameIsNotAvailableDomainException when try to persist another record with the same username")
    void shouldThrowExceptionWhenUsernameIsNotAvailable () {
        when(userRepository.existUsername("doe@gmail.com"))
                .thenReturn(Mono.just(Boolean.TRUE));

        CreateUserCommand cmd = new CreateUserCommand("doe@gmail.com", "xxx");

        StepVerifier.create(userUseCase.createUser(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Username doe@gmail.com is not available", throwable.getMessage());
                    assertInstanceOf(UsernameIsNotAvailableDomainException.class, throwable);
                }).verify();

        verify(userRepository, Mockito.times(1)).existUsername(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenDBIsNotWorking () {
        when(userRepository.existUsername("doe@gmail.com"))
                .thenReturn(Mono.just(Boolean.FALSE));

        when(userRepository.save(any(User.class)))
                .thenReturn(Mono.error(new RuntimeException("DB is not working")));

        CreateUserCommand cmd = new CreateUserCommand("doe@gmail.com", "xxx");

        StepVerifier.create(userUseCase.createUser(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("DB is not working", throwable.getMessage());
                    assertInstanceOf(Exception.class, throwable);
                }).verify();

        verify(userRepository, Mockito.times(1)).existUsername("doe@gmail.com");
        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    void shouldBePersistedUserWithSuccessful () {
        User expected = User.create("doe@gmail.com", "xxx");

        when(userRepository.existUsername("doe@gmail.com"))
                .thenReturn(Mono.just(Boolean.FALSE));

        when(userRepository.save(expected))
                .thenReturn(Mono.just(expected));

        CreateUserCommand cmd = new CreateUserCommand("doe@gmail.com", "xxx");

        StepVerifier.create(userUseCase.createUser(cmd))
                .expectNext(expected)
                .verifyComplete();

        verify(userRepository, Mockito.times(1)).existUsername(anyString());
        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

}
