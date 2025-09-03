package org.pragma.creditya.model.user.exception;

import org.pragma.creditya.model.shared.exception.DomainException;

public class UsernameIsNotAvailableDomainException extends DomainException {
    public UsernameIsNotAvailableDomainException(String message) {
        super(message);
    }
}
