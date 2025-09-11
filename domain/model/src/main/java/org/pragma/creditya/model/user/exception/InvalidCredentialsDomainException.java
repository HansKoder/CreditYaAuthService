package org.pragma.creditya.model.user.exception;

import org.pragma.creditya.model.shared.exception.DomainException;

public class InvalidCredentialsDomainException extends DomainException {
    public InvalidCredentialsDomainException(String message) {
        super(message);
    }
}
