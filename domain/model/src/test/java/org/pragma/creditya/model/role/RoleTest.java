package org.pragma.creditya.model.role;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.role.exception.RoleDomainException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoleTest {

    @Test
    void shouldThrowException_WhenRoleNameIsNull () {
        RoleDomainException exception = assertThrows(RoleDomainException.class, ()-> Role.RoleBuilder.aRole().name(" ").build() );
        assertEquals("Name must be mandatory", exception.getMessage());
    }

}
