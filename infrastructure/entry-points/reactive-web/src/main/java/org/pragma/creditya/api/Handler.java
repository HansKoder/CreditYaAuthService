package org.pragma.creditya.api;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.api.dto.request.CreateUserRequest;
import org.pragma.creditya.api.mapper.UserRestMapper;
import org.pragma.creditya.usecase.user.ports.in.IUserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final IUserUseCase userUseCase;

    public Mono<ServerResponse> createUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserRequest.class)
                .map(UserRestMapper::toCommand)
                .flatMap(userUseCase::createUser)
                .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).build());
    }

}
