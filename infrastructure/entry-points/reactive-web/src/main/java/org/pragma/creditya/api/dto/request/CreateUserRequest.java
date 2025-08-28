package org.pragma.creditya.api.dto.request;

public record CreateUserRequest(String username, String password) {

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
