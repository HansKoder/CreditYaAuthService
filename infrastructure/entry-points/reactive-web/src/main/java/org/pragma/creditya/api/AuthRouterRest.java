package org.pragma.creditya.api;

import org.pragma.creditya.api.dto.response.ErrorResponse;
import org.pragma.creditya.infracommon.exception.InfrastructureException;
import org.pragma.creditya.model.user.exception.UserDomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouterRest {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthRouterRest.class);

    @Bean
    public RouterFunction<ServerResponse> routerFunction(AuthHandler handler) {
        return route(POST("/api/v1/user"), handler::createUser)
                .filter(domainErrorMapper())
                .filter(infraErrorHandler())
                .filter(unexpectedErrorHandler())
                .andRoute(POST("/api/v1/login"), handler::login);
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> domainErrorMapper() {
        return (request, next) ->
                next.handle(request)
                        .onErrorResume(UserDomainException.class, ex ->
                                ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()))
                        )
                        .onErrorResume(Exception.class, ex ->
                                ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()))
                        );
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> infraErrorHandler () {

        return (request, next) ->
                next.handle(request)
                        .onErrorResume(InfrastructureException.class, ex ->
                                ServerResponse.status(HttpStatus.valueOf(ex.getStatus())).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(ex.getStatus(), ex.getType().name().concat(" | ").concat(ex.getMessage())))
                        )
                        .log()
                        .doOnSuccess(e -> LOGGER.info("[infra.web-reactive] exceptions infra -> send error "));
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> unexpectedErrorHandler () {
        return (request, next) ->
                next.handle(request)
                        .onErrorResume(Exception.class, ex ->
                                ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()))
                        ).log()
                        .doOnSuccess(e -> LOGGER.info("[infra.web-reactive] exceptions unexpected -> send error, status response. "));
    }

}
