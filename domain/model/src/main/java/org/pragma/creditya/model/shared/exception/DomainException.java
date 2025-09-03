package org.pragma.creditya.model.shared.exception;

public class DomainException extends RuntimeException{
    public DomainException() {
    }

    public DomainException(String message) {
        super(message);
    }
}
