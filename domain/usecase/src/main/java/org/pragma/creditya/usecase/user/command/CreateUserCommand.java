package org.pragma.creditya.usecase.user.command;

public record CreateUserCommand (
        String username,
        String password,
        Long roleId
) {}
