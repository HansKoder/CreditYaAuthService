package org.pragma.creditya.model.shared.model.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BaseIdTest {

    private static class DummyId extends BaseId<String> {
        protected DummyId(String value) {
            super(value);
        }
    }

    @Test
    void shouldBeEqualsTwoIDs () {
        DummyId dummyIdA = new DummyId("abc");
        DummyId dummyIdB = new DummyId("abc");

        assertEquals(dummyIdA, dummyIdB);
    }

    @Test
    void shouldBeDistinctTwoIDs () {
        DummyId dummyIdA = new DummyId("abc");
        DummyId dummyIdB = new DummyId("AbC");

        assertNotEquals(dummyIdA, dummyIdB);
    }

    @Test
    void shouldHaveTheSameHash () {
        DummyId dummyIdA = new DummyId("abc");
        DummyId dummyIdB = new DummyId("abc");

        assertEquals(dummyIdA.hashCode(), dummyIdB.hashCode());
    }

    @Test
    void shouldHaveDistinctHash () {
        DummyId dummyIdA = new DummyId("abc");
        DummyId dummyIdB = new DummyId("AbC");

        assertNotEquals(dummyIdA.hashCode(), dummyIdB.hashCode());
    }

}
