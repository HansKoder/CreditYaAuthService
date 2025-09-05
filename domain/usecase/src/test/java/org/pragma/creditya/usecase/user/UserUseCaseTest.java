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

    private final String EXAMPLE_ENCODE_PASS = "$2y$10$X0Ix./OAjN8.RgkolvdYzOmSBGuTFIsKfu6BmYFBPVMgdcNyD2sxK";

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

        CreateUserCommand cmd = new CreateUserCommand(null, "123456");
        StepVerifier.create(userUseCase.createUser(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Username must be mandatory", throwable.getMessage());
                    assertInstanceOf(UserDomainException.class, throwable);
                }).verify();
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsEmpty () {
        when(encodeProvider.encode(anyString()))
                .thenReturn("");

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
        when(encodeProvider.encode(anyString()))
                .thenReturn(EXAMPLE_ENCODE_PASS);

        when(userRepository.existUsername("doe@gmail.com"))
                .thenReturn(Mono.just(Boolean.TRUE));

        CreateUserCommand cmd = new CreateUserCommand("doe@gmail.com", "xxx");

        StepVerifier.create(userUseCase.createUser(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Username doe@gmail.com is not available", throwable.getMessage());
                    assertInstanceOf(UsernameIsNotAllowedDomainException.class, throwable);
                }).verify();

        verify(userRepository, Mockito.times(1)).existUsername(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenDBIsNotWorking () {
        when(encodeProvider.encode(anyString()))
                .thenReturn(EXAMPLE_ENCODE_PASS);

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
        User expected = User.createUser("doe@gmail.com", "xxx");

        when(encodeProvider.encode(anyString()))
                .thenReturn(EXAMPLE_ENCODE_PASS);

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
