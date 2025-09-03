package org.pragma.creditya.model.user.exception;

import org.pragma.creditya.model.shared.exception.DomainException;

public class UserDomainException extends DomainException {
    public UserDomainException(String message) {
        super(message);
    }
}
