package org.pragma.creditya.model.user.valueobject;

import org.pragma.creditya.model.user.exception.UserDomainException;

public record Retry(Integer cant) {

    public Retry {
        if (cant == null)
            throw new UserDomainException("Retry must be mandatory");

        if (cant < 0)
            throw new UserDomainException("Retry must be positive");
    }

    public Retry decrease() {
        if (cant == 0)
            throw new UserDomainException("Limit to decrease is until zero");

        return new Retry(cant - 1);
    }
}
