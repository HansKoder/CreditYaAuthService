package org.pragma.creditya.model.user.valueobject;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.user.exception.UserDomainException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserNameTest {

    @Test
    void shouldThrowWhenUsernameIsNull () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> new UserName(null));
        assertEquals("Username must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowWhenUsernameIsEmpty () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> new UserName(" "));
        assertEquals("Username must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowWhenUsernameHasAnInvalidFormatEmail () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> new UserName("bad format"));
        assertEquals("Username should have an valid email format", exception.getMessage());
    }

    @Test
    void shouldBeCreatedWithSuccessful () {
        assertEquals("doe@gmail.com", new UserName("doe@gmail.com").getValue());
    }

}
