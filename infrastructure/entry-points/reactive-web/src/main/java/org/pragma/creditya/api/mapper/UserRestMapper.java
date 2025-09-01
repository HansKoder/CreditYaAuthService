package org.pragma.creditya.api.mapper;

import org.pragma.creditya.api.dto.request.CreateUserRequest;
import org.pragma.creditya.api.dto.response.GetUserResponse;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;

public class UserRestMapper {

    private UserRestMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CreateUserCommand toCommand (CreateUserRequest request) {
        return new CreateUserCommand(request.username(), request.password());
    }

    public static GetUserResponse toResponse (User user) {
        return new GetUserResponse(
          user.getId().getValue().toString(),
          user.getUserName().getValue()
        );
    }

}
