package org.pragma.creditya.usecase.user.command;

public record CreateUserCommand (
        String username,
        String password
) {
    @Override
    public String toString() {
        return "CreateUserCommand{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
