package org.pragma.creditya.model.shared.model.valueobject;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.role.exception.RoleDomainException;

import static org.junit.jupiter.api.Assertions.*;


public class RoleIdTest {

    @Test
    @DisplayName("Should be created with success, when id is null, this field is not mandatory")
    void shouldThrowException_WhenIdIsNull () {
        RoleDomainException exp = assertThrows(RoleDomainException.class, () -> new RoleId(null));
        assertEquals("Role Id must be mandatory", exp.getMessage());
    }

    @Test
    @DisplayName("Should be created with success, when id is persisted, this field is not mandatory")
    void shouldBeOK_WhenIdExists () {
        assertDoesNotThrow(() -> new RoleId(1L));
    }


}
