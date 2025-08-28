package org.pragma.creditya.model.user;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.user.exception.UserDomainException;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void shouldThrowWhenUsernameIsNull () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> {
            User.create(null,"password");
        });

        assertEquals("Username must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowWhenUsernameIsEmpty () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> {
            User.create("", "password");
        });

        assertEquals("Username must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowWhenUsernameHasAnInvalidFormatEmail () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> {
            User.create("jhon.123", "password");
        });

        assertEquals("Username should have an valid email format", exception.getMessage());
    }

    @Test
    void shouldThrowWhenPasswordIsNull () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> {
            User.create("jhon.doe@gmail.com", null);
        });

        assertEquals("Password must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowWhenPasswordIsEmpty () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> {
            User.create("jhon.doe@gmail.com", " ");
        });

        assertEquals("Password must be mandatory", exception.getMessage());
    }

    @Test
    void shouldCreateUserWithSuccess () {
        User domain = User.create("jhon.doe@gmail.com", "password");
        assertNotNull(domain);
        assertNotNull(domain.getPassword().value());
        assertNotNull(domain.getId().getValue());

        assertEquals("jhon.doe@gmail.com", domain.getId().getValue());
        assertEquals("password", domain.getPassword().value());
    }

}
