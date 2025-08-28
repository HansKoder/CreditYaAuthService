package org.pragma.creditya.model.user.valueobject;

import org.pragma.creditya.model.user.exception.UserDomainException;

public record Password (String value)  {
    public Password {
        if (value == null || value.isBlank())
            throw new UserDomainException("Password must be mandatory");
    }
}
