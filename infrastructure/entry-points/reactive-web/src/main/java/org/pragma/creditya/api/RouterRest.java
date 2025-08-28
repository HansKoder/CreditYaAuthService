package org.pragma.creditya.api;

import org.pragma.creditya.api.dto.response.ErrorResponse;
import org.pragma.creditya.model.user.exception.UserDomainException;
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
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/auth"), handler::createUser)
                .filter(domainErrorMapper());
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

}
