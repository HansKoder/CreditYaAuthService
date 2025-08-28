package org.pragma.creditya.api.mapper;

import org.pragma.creditya.api.dto.request.CreateUserRequest;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;

public class UserMapper {

    public static CreateUserCommand toCommand (CreateUserRequest request) {
        System.out.println("Mapper to command " + request.toString());
        CreateUserCommand cmd = new CreateUserCommand(request.username(), request.password());
        System.out.println("Mapped " + cmd.toString());
        return cmd;
    }

}
