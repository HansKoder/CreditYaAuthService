package org.pragma.creditya.api.filter;

import org.pragma.creditya.infracommon.exception.InfraErrorType;
import org.pragma.creditya.infracommon.exception.InfrastructureException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends InfrastructureException {

    public InvalidTokenException(String message) {
        super(message, HttpStatus.FORBIDDEN.value(), InfraErrorType.WEB_REACTIVE);
    }
}
