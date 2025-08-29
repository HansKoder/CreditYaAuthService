package org.pragma.creditya.api.config;

import org.pragma.creditya.api.Handler;
import org.pragma.creditya.api.RouterRest;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.api.dto.request.CreateUserRequest;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;
import org.pragma.creditya.usecase.user.ports.in.IUserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    IUserUseCase userUseCase;

    @Test
    void corsConfigurationShouldAllowOrigins() {
        User user = User.create("doe@gmail.com", "123");
        when(userUseCase.createUser(any(CreateUserCommand.class)))
                .thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/api/auth")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserRequest(" ", "123"))
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