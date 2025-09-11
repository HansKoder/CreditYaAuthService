package org.pragma.creditya.model.user;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.user.exception.UserLockedDomainException;

import static org.junit.jupiter.api.Assertions.*;

public class UserLoginTest {

    @Test
    void shouldThrowException_UserIsLocked () {
        UserLockedDomainException exception = assertThrows(UserLockedDomainException.class, () -> {
            User user = User.Builder.anUser()
                    .userName("doe@gmail.com")
                    .password("123")
                    .lock(Boolean.TRUE)
                    .build();

            user.checkIsLocked();
        });

        assertEquals("User doe@gmail.com is Locked, invalid any operation until new order", exception.getMessage());
    }


    @Test
    void shouldBeFalse_WhenUserShouldBeBlocked () {
        User user = User.Builder.anUser()
                .userName("doe@gmail.com")
                .password("123")
                .lock(Boolean.FALSE)
                .retry(0)
                .build();


        assertTrue(user.shouldBeBlocked());
    }

    private User test_userHadFailedTwice () {
        User user = User.Builder.anUser()
                .userName("doe@gmail.com")
                .password("123")
                .lock(Boolean.FALSE)
                .retry(User.DEFAULT_THRESHOLD)
                .build();

        // User had failed once
        user.loginFailed();

        assertEquals(2, user.getRetry().cant());
        assertDoesNotThrow(user::checkIsLocked);
        assertFalse(user.shouldBeBlocked());

        // User had failed twice
        user.loginFailed();

        assertEquals(1, user.getRetry().cant());
        assertDoesNotThrow(user::checkIsLocked);
        assertFalse(user.shouldBeBlocked());

        return user;
    }

    @Test
    void shouldBeAvailableUser_whenFailedLoginTwice () {
        test_userHadFailedTwice();
    }

    @Test
    void shouldBeBlocked_WhenUserHadFailedThreeTimes () {
        User user = test_userHadFailedTwice();

        user.loginFailed();

        assertEquals(0, user.getRetry().cant());
        assertDoesNotThrow(user::checkIsLocked);
        assertTrue(user.shouldBeBlocked());
    }

}
