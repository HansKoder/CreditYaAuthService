package org.pragma.creditya.api;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.api.dto.request.CreateUserRequest;
import org.pragma.creditya.api.dto.response.ErrorResponse;
import org.pragma.creditya.api.dto.response.GetUserResponse;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.model.user.exception.UserDomainException;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;
import org.pragma.creditya.usecase.user.ports.in.IUserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AuthRouterRest.class, AuthHandler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    IUserUseCase userUseCase;

    @Test
    void shouldCreateUserWithSuccessful() {
        UUID userId = UUID.fromString("5b87a0d6-2fed-4db7-aa49-49663f719659");
        // User user = User.rebuild(userId, "doe@gmail.com", "123");
        User user = User.Builder.anUser()
                .id(userId)
                .userName("doe@gmail.com")
                .password("123")
                .build();

        when(userUseCase.createUser(any(CreateUserCommand.class)))
                        .thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/api/auth")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserRequest("doe@gmail.com", "123"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(GetUserResponse.class)
                .value(persisted -> {
                    assertEquals("5b87a0d6-2fed-4db7-aa49-49663f719659", persisted.userId());
                    assertEquals("doe@gmail.com", persisted.username());
                });
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsEmpty() {
        when(userUseCase.createUser(any(CreateUserCommand.class)))
                        .thenReturn(Mono.error(new UserDomainException("Username must be mandatory")));

        webTestClient.post()
                .uri("/api/auth")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserRequest(" ", "123"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                            assertEquals(400, errorResponse.status());
                            assertEquals("Username must be mandatory", errorResponse.message());
                        }
                );;

    }

    @Test
    void shouldThrowExceptionWhenSQLHasInvalidQuery() {
        when(userUseCase.createUser(any(CreateUserCommand.class)))
                        .thenReturn(Mono.error(new SQLException("Bad SQL")));

        webTestClient.post()
                .uri("/api/auth")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserRequest("doe@gmail.com", "123"))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                            assertEquals(500, errorResponse.status());
                            assertEquals("Bad SQL", errorResponse.message());
                        }
                );;

    }

    @Test
    void shouldThrowExceptionWhenDBIsNotWorking() {
        when(userUseCase.createUser(any(CreateUserCommand.class)))
                        .thenReturn(Mono.error(new RuntimeException("DB is not working")));

        webTestClient.post()
                .uri("/api/auth")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserRequest("doe@gmail.com", "123"))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                            assertEquals(500, errorResponse.status());
                            assertEquals("DB is not working", errorResponse.message());
                        }
                );;

    }
}
