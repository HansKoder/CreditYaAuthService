package org.pragma.creditya.model.role.valueobject;

import org.pragma.creditya.model.role.exception.RoleDomainException;

public record RoleName (String name) {

    public RoleName {
        if (name == null || name.isBlank())
            throw new RoleDomainException("Name must be mandatory");
    }

}
