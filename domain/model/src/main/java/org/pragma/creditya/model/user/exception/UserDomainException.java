package org.pragma.creditya.model.user.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class UserDomainException extends DomainException {
    public UserDomainException() {
    }

    public UserDomainException(String message) {
        super(message);
    }
}
