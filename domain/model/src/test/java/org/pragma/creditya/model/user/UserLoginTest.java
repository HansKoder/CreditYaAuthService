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

            user.checkLogin();
        });

        assertEquals("User doe@gmail.com is Locked, invalid any operation until new order", exception.getMessage());
    }


    @Test
    void shouldThrowException_UserDoesNotHaveOtherRetry () {
        UserLockedDomainException exception = assertThrows(UserLockedDomainException.class, () -> {
            User user = User.Builder.anUser()
                    .userName("doe@gmail.com")
                    .password("123")
                    .lock(Boolean.FALSE)
                    .retry(0)
                    .build();

            user.checkLogin();
        });

        assertEquals("User doe@gmail.com is Locked, invalid any operation until new order", exception.getMessage());
    }

    private User test_takeThreeRetries () {
        User user = User.Builder.anUser()
                .userName("doe@gmail.com")
                .password("123")
                .lock(Boolean.FALSE)
                .retry(User.DEFAULT_THRESHOLD)
                .build();

        // Try Login once
        user.checkLogin();

        assertEquals(2, user.getRetry().cant());
        assertFalse(user.getLock().isLock());

        // Try Login second time
        user.checkLogin();

        assertEquals(1, user.getRetry().cant());
        assertFalse(user.getLock().isLock());

        // Try Login three time
        user.checkLogin();

        assertEquals(0, user.getRetry().cant());
        assertFalse(user.getLock().isLock());

        return user;
    }

    @Test
    void shouldBeOK_whenTakeAnewRetry_forLoginUntilThreeTimes () {
        test_takeThreeRetries();
    }

    @Test
    void shouldBeThrowException_whenRunOutRetry () {
        User user = test_takeThreeRetries();

        // Run out
        UserLockedDomainException exception = assertThrows(UserLockedDomainException.class, user::checkLogin);
        assertEquals("User doe@gmail.com is Locked, invalid any operation until new order", exception.getMessage());
    }



}
