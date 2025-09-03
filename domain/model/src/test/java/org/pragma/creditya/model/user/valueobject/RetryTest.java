package org.pragma.creditya.model.user.valueobject;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.user.exception.UserDomainException;

import static org.junit.jupiter.api.Assertions.*;

public class RetryTest {

    private final int THRESHOLD = 3;

    @Test
    void shouldThrowException_retryMustBeMandatory () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> new Retry(null));
        assertEquals("Retry must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowException_retryMustBePositive () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> new Retry(-5));

        assertEquals("Retry must be positive", exception.getMessage());
    }

    @Test
    void shouldBeCreatedWithSuccessful () {
        assertEquals(3, new Retry(THRESHOLD).cant());
    }

    @Test
    void shouldDecreaseCant_becauseInvalidCredentials () {
        Retry retryWithDefaultCant = new Retry(THRESHOLD);

        Retry retryDecreased = retryWithDefaultCant.decrease();

        assertNotEquals(retryDecreased, retryWithDefaultCant);
        assertEquals(2, retryDecreased.cant());
    }

    @Test
    void shouldThrowException_whenDecreaseLowerThanZero () {
        UserDomainException exception = assertThrows(UserDomainException.class, () -> {
            Retry retryWithDefaultCant = new Retry(THRESHOLD);

            retryWithDefaultCant
                    .decrease()
                    .decrease()
                    .decrease()
                    .decrease();
        });

        assertEquals("Limit to decrease is until zero", exception.getMessage());
    }

}
