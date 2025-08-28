package org.pragma.creditya.api.mapper;

import org.pragma.creditya.api.dto.request.CreateUserRequest;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;

public class UserMapper {

    public static CreateUserCommand toCommand (CreateUserRequest request) {
        return new CreateUserCommand(request.username(), request.password());
    }

}
