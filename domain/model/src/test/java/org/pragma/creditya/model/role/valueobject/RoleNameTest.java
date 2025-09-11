package org.pragma.creditya.model.role.valueobject;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.role.exception.RoleDomainException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoleNameTest {

    @Test
    void shouldThrowWhenPasswordIsNull () {
        RoleDomainException exception = assertThrows(RoleDomainException.class, () -> new RoleName(null));
        assertEquals("Name must be mandatory", exception.getMessage());
    }

        @Test
    void shouldThrowWhenPasswordIsEmpty () {
        RoleDomainException exception = assertThrows(RoleDomainException.class, () -> new RoleName(" "));
        assertEquals("Name must be mandatory", exception.getMessage());
    }

}
