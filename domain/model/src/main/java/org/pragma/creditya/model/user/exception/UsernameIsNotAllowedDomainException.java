package org.pragma.creditya.model.user.exception;

import org.pragma.creditya.model.shared.exception.DomainException;

public class UsernameIsNotAllowedDomainException extends DomainException {
    public UsernameIsNotAllowedDomainException(String message) {
        super(message);
    }
}
