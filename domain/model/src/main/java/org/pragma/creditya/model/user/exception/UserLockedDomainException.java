package org.pragma.creditya.model.user.exception;

import org.pragma.creditya.model.shared.exception.DomainException;

public class UserLockedDomainException extends DomainException {
    public UserLockedDomainException(String message) {
        super(message);
    }
}
