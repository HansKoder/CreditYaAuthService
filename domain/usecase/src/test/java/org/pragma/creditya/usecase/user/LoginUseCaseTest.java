package org.pragma.creditya.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.model.user.exception.InvalidCredentialsDomainException;
import org.pragma.creditya.model.user.exception.UserDomainException;
import org.pragma.creditya.model.user.exception.UserLockedDomainException;
import org.pragma.creditya.model.user.gateways.EncodeProvider;
import org.pragma.creditya.model.user.gateways.TokenProvider;
import org.pragma.creditya.model.user.gateways.UserRepository;
import org.pragma.creditya.usecase.user.command.LoginCommand;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;


public class LoginUseCaseTest {

    @InjectMocks
    private LoginUseCase useCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private EncodeProvider encodeProvider;

    private final String EXAMPLE_ENCODE_PASS = "$2y$10$X0Ix./OAjN8.RgkolvdYzOmSBGuTFIsKfu6BmYFBPVMgdcNyD2sxK";

    private final String EXAMPLE_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";

    private final User USER_SHOULD_BE_LOCKED = User.Builder.anUser()
            .userName("user@gmail.com")
            .password("123")
            .lock(Boolean.FALSE)
            .retry(0)
            .build();

    private final User USER_IS_LOCKED = User.Builder.anUser()
            .userName("user@gmail.com")
            .password("123")
            .lock(Boolean.TRUE)
            .retry(0)
            .build();

    private final User USER = User.Builder.anUser()
            .userName("user@gmail.com")
            .password("123")
            .lock(Boolean.FALSE)
            .retry(User.DEFAULT_THRESHOLD)
            .build();

    private final User USER_ONE_RETRY = User.Builder.anUser()
            .userName("user@gmail.com")
            .password("123")
            .lock(Boolean.FALSE)
            .retry(1)
            .build();

    private final User USER_TWO_RETRY = User.Builder.anUser()
            .userName("user@gmail.com")
            .password("123")
            .lock(Boolean.FALSE)
            .retry(1)
            .build();

    @BeforeEach
    void setup () {
        userRepository = Mockito.mock(UserRepository.class);
        tokenProvider = Mockito.mock(TokenProvider.class);
        encodeProvider = Mockito.mock(EncodeProvider.class);

        useCase = new LoginUseCase(userRepository, tokenProvider, encodeProvider);
    }

    @Test
    void shouldThrowException_WhenUserNameIsNull () {
        LoginCommand cmd = new LoginCommand(null, "123456");

        var response = useCase.handler(cmd);
        StepVerifier.create(response)
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Username must be mandatory", throwable.getMessage());
                    assertInstanceOf(UserDomainException.class, throwable);
                }).verify();
    }

    @Test
    void shouldThrowException_WhenPasswordIsNull () {
        LoginCommand cmd = new LoginCommand("user@gmail.com", " ");

        var response = useCase.handler(cmd);
        StepVerifier.create(response)
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Password must be mandatory", throwable.getMessage());
                    assertInstanceOf(UserDomainException.class, throwable);
                }).verify();
    }

    @Test
    void shouldThrowException_whenUserNameIsNotFound () {

        Mockito.when(userRepository.findByUsername("user@gmail.com"))
                .thenReturn(Mono.empty());

        var response = useCase.handler(new LoginCommand("user@gmail.com", "123"));

        StepVerifier.create(response)
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Invalid Credentials", throwable.getMessage());
                    assertInstanceOf(InvalidCredentialsDomainException.class, throwable);
                }).verify();
    }

    @Test
    void shouldThrowException_whenUserIsLockedBeforeStaringLogin () {
        Mockito.when(userRepository.findByUsername("user@gmail.com"))
                .thenReturn(Mono.just(USER_IS_LOCKED));

        var response = useCase.handler(new LoginCommand("user@gmail.com", "123"));

        StepVerifier.create(response)
                .expectErrorSatisfies(throwable -> {
                    assertEquals("User user@gmail.com is Locked, invalid any operation until new order", throwable.getMessage());
                    assertInstanceOf(UserLockedDomainException.class, throwable);
                }).verify();
    }

    @Test
    void shouldThrowException_BecauseUserShouldBeLocked () {
        Mockito.when(userRepository.findByUsername("user@gmail.com"))
                .thenReturn(Mono.just(USER_SHOULD_BE_LOCKED));

        Mockito.when(userRepository.save(USER_IS_LOCKED))
                .thenReturn(Mono.just(USER_IS_LOCKED));

        var response = useCase.handler(new LoginCommand("user@gmail.com", "123"));

        StepVerifier.create(response)
                .expectErrorSatisfies(throwable -> {
                    assertEquals("User user@gmail.com is Locked, invalid any operation until new order", throwable.getMessage());
                    assertInstanceOf(UserLockedDomainException.class, throwable);
                }).verify();
    }

    @Test
    void shouldThrowException_WhenPassIsIncorrect () {
        Mockito.when(userRepository.findByUsername("user@gmail.com"))
                .thenReturn(Mono.just(USER));

        Mockito.when(userRepository.save(USER_TWO_RETRY))
                .thenReturn(Mono.just(USER_TWO_RETRY));

        Mockito.when(encodeProvider.encode("123"))
                .thenReturn(EXAMPLE_ENCODE_PASS);

        Mockito.when(encodeProvider.matches("123", EXAMPLE_ENCODE_PASS))
                .thenReturn(Boolean.FALSE);

        var response = useCase.handler(new LoginCommand("user@gmail.com", "123"));

        StepVerifier.create(response)
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Invalid Credentials", throwable.getMessage());
                    assertInstanceOf(InvalidCredentialsDomainException.class, throwable);
                }).verify();
    }

    @Test
    void shouldThrowException_WhenHadFailedThreeTimesIsLocked () {
        Mockito.when(userRepository.findByUsername("user@gmail.com"))
                .thenReturn(Mono.just(USER_ONE_RETRY));

        Mockito.when(userRepository.save(USER_IS_LOCKED))
                .thenReturn(Mono.just(USER_IS_LOCKED));

        Mockito.when(encodeProvider.encode("123"))
                .thenReturn(EXAMPLE_ENCODE_PASS);

        Mockito.when(encodeProvider.matches("123", EXAMPLE_ENCODE_PASS))
                .thenReturn(Boolean.FALSE);

        var response = useCase.handler(new LoginCommand("user@gmail.com", "123"));

        StepVerifier.create(response)
                .expectErrorSatisfies(throwable -> {
                    assertEquals("User user@gmail.com is Locked, invalid any operation until new order", throwable.getMessage());
                    assertInstanceOf(UserLockedDomainException.class, throwable);
                }).verify();
    }

    @Test
    void shouldReturnAToken_BecauseLoginIsValid () {
        Mockito.when(userRepository.findByUsername("user@gmail.com"))
                .thenReturn(Mono.just(USER));

        Mockito.when(encodeProvider.encode("123"))
                .thenReturn(EXAMPLE_ENCODE_PASS);

        Mockito.when(encodeProvider.matches("123", EXAMPLE_ENCODE_PASS))
                .thenReturn(Boolean.TRUE);

        Mockito.when(tokenProvider.generateToken(USER))
                .thenReturn(Mono.just(EXAMPLE_JWT));

        var response = useCase.handler(new LoginCommand("user@gmail.com", "123"));

        StepVerifier.create(response)
                .expectNext(EXAMPLE_JWT)
                .verifyComplete();
    }


}
