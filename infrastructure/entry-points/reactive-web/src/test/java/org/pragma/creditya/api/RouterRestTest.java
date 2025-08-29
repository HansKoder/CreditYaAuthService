package org.pragma.creditya.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.pragma.creditya.api.dto.request.CreateUserRequest;
import org.pragma.creditya.api.dto.response.ErrorResponse;
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

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    IUserUseCase userUseCase;

    @Test
    void shouldCreateUserWithSuccessful() {
        User user = User.create("doe@gmail.com", "123");
        when(userUseCase.createUser(any(CreateUserCommand.class)))
                        .thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/api/auth")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserRequest("doe@gmail.com", "123"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Void.class);
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
}
