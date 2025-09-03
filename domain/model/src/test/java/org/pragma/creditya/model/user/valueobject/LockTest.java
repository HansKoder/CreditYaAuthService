package org.pragma.creditya.model.user.valueobject;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.user.exception.UserDomainException;

import static org.junit.jupiter.api.Assertions.*;

public class LockTest {

    @Test
    void shouldThrowException_whenLockIsMandatory () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> new Lock(null));
        assertEquals("Lock must be mandatory", exception.getMessage());
    }

    @Test
    void shouldBeCreatedWithSuccessful () {
        assertTrue(new Lock(Boolean.TRUE).isLock());
    }

    @Test
    void shouldBeLocked () {
        Lock lock = new Lock(Boolean.FALSE);
        assertTrue(lock.enabled().isLock());
    }

    @Test
    void shouldBeUnLocked () {
        Lock lock = new Lock(Boolean.TRUE);
        assertFalse(lock.disabled().isLock());
    }

}
