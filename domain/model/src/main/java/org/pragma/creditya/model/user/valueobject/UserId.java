package org.pragma.creditya.model.user.valueobject;

import org.pragma.creditya.model.shared.model.valueobject.BaseId;

import java.util.UUID;

public class UserId extends BaseId<UUID> {
    public UserId(UUID value) {
        super(value);
    }
}
