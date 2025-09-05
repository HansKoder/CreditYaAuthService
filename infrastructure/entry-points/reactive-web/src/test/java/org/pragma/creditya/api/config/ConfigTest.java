package org.pragma.creditya.api.config;

import org.mockito.Mock;
import org.pragma.creditya.api.AuthHandler;
import org.pragma.creditya.api.AuthRouterRest;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.api.dto.request.CreateUserRequest;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;
import org.pragma.creditya.usecase.user.ports.in.ILoginUseCase;
import org.pragma.creditya.usecase.user.ports.in.IUserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AuthRouterRest.class, AuthHandler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    IUserUseCase userUseCase;

    @MockitoBean
    ILoginUseCase loginUseCase;

    @Test
    void corsConfigurationShouldAllowOrigins() {
        UUID userId = UUID.fromString("5b87a0d6-2fed-4db7-aa49-49663f719659");
        // User user = User.rebuild(userId,"doe@gmail.com", "123");
        User user = User.Builder.anUser()
                .id(userId)
                .userName("doe@gmail.com")
                .password("123")
                .lock(false)
                .retry(3)
                .build();

        when(userUseCase.createUser(any(CreateUserCommand.class)))
                .thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/api/v1/user")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserRequest("doe@gmail.com", "123"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}