package org.pragma.creditya.model.user.valueobject;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.user.exception.UserDomainException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordTest {


    @Test
    void shouldThrowWhenPasswordIsNull () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> new Password(null));
        assertEquals("Password must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowWhenPasswordIsEmpty () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> new Password(" "));
        assertEquals("Password must be mandatory", exception.getMessage());
    }

}
