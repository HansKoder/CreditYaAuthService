package org.pragma.creditya.model.role.exception;

import org.pragma.creditya.model.shared.exception.DomainException;

public class RoleIsNotFoundDomainException extends DomainException {
    public RoleIsNotFoundDomainException(String message) {
        super(message);
    }
}
