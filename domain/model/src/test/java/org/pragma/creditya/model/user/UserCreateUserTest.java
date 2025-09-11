package org.pragma.creditya.model.user;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.user.exception.UserDomainException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserCreateUserTest {


    @Test
    void shouldThrowException_whenUserNameIsNull () {
        UserDomainException exception = assertThrows(UserDomainException.class,
                () -> User.createUser(null, "123"));

        assertEquals("Username must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenUserPasswordIsNull () {
        UserDomainException exception = assertThrows(UserDomainException.class, () ->
                User.createUser("doe@gmail.com", " "));

        assertEquals("Password must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowException_creationUserIsInvalid () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> {
            User user = User.Builder
                    .anUser()
                    .id(UUID.fromString("4fc59fe1-ad34-4663-9143-58bc99927b32"))
                    .userName("doe@gmail.com")
                    .password("123")
                    .build();

            user.checkCreationUser();
        });

        assertEquals("User initialization must be without ID", exception.getMessage());
    }


    @Test
    void shouldBeCreatedUserWithSuccessful () {
        User user = User.createUser("doe@gmail.com", "123");
        user.checkCreationUser();

        assertNull(user.getId());
        assertFalse(user.getLock().isLock());
        assertEquals(User.DEFAULT_THRESHOLD, user.getRetry().cant());
    }




}
