package org.pragma.creditya.security.exception;

import org.pragma.creditya.infracommon.exception.InfraErrorType;
import org.pragma.creditya.infracommon.exception.InfrastructureException;
import org.springframework.http.HttpStatus;

public class SecurityInfraException extends InfrastructureException {
    public SecurityInfraException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR.value(), InfraErrorType.SECURITY);
    }
}
