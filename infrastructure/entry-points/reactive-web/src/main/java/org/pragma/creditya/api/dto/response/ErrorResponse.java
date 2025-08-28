package org.pragma.creditya.api.dto.response;

public record  ErrorResponse (
        int status,
        String message
) { }

