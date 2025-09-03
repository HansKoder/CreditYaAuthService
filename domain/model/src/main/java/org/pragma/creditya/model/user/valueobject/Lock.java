package org.pragma.creditya.model.user.valueobject;

import org.pragma.creditya.model.user.exception.UserDomainException;

public record Lock(Boolean isLock) {
    public Lock {
        if (isLock == null)
            throw new UserDomainException("Lock must be mandatory");
    }

    public Lock enabled() {
        return new Lock(Boolean.TRUE);
    }
    public Lock disabled() {
        return new Lock(Boolean.FALSE);
    }
}
