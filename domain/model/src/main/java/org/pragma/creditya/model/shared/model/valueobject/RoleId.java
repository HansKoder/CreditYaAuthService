package org.pragma.creditya.model.shared.model.valueobject;

import org.pragma.creditya.model.role.exception.RoleDomainException;

public record RoleId (Long id) {
    public RoleId {
        if (id == null)
            throw new RoleDomainException("Role Id must be mandatory");
    }
}
