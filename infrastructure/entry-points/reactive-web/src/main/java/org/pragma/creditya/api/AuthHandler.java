package org.pragma.creditya.api;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.api.dto.request.CreateUserRequest;
import org.pragma.creditya.api.dto.request.LoginRequest;
import org.pragma.creditya.api.mapper.UserRestMapper;
import org.pragma.creditya.usecase.IAuthApplicationService;
import org.pragma.creditya.usecase.user.ports.in.ILoginUseCase;
import org.pragma.creditya.usecase.user.ports.in.IUserUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final IAuthApplicationService authApplicationService;

    private final Logger logger = LoggerFactory.getLogger(AuthHandler.class);

    public Mono<ServerResponse> createUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserRequest.class)
                .map(UserRestMapper::toCommand)
                .flatMap(authApplicationService::createUser)
                .map(UserRestMapper::toResponse)
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED).bodyValue(response));
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequest.class)
                .map(UserRestMapper::toCommand)
                .flatMap(authApplicationService::login)
                .flatMap(token -> ServerResponse.status(HttpStatus.OK).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build())
                .doOnSuccess(e -> logger.info("[infra.reactive-web] (login) was successful"));
    }

}
