package org.pragma.creditya.infracommon.exception;

import lombok.Getter;

@Getter
public abstract class InfrastructureException extends RuntimeException {

    private final int status;
    private final InfraErrorType type;

    public InfrastructureException(String message, int status, InfraErrorType type) {
        super(message);
        this.status = status;
        this.type = type;
    }
}
