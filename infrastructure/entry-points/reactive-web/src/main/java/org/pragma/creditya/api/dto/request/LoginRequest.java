package org.pragma.creditya.api.dto.request;

public record LoginRequest (
        String username,
        String password
) { }
