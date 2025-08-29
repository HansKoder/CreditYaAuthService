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

    private static class AnotherDummyId extends BaseId<String> {
        protected AnotherDummyId(String value) {
            super(value);
        }
    }

    @Test
    void shouldBeEqualsTwoIDs () {
        DummyId dummyIdA = new DummyId("abc");
        DummyId dummyIdB = new DummyId("abc");

        assertEquals(dummyIdA, dummyIdB);
        assertEquals(dummyIdA.hashCode(), dummyIdB.hashCode());
    }

    @Test
    void shouldBeDistinctTwoIDs () {
        DummyId dummyIdA = new DummyId("abc");
        DummyId dummyIdB = new DummyId("AbC");

        assertNotEquals(dummyIdA, dummyIdB);
        assertNotEquals(dummyIdA.hashCode(), dummyIdB.hashCode());
    }


    @Test
    void shouldBeDistinctOneIsIdNull () {
        DummyId dummyIdA = new DummyId(null);
        DummyId dummyIdB = new DummyId("AbC");

        assertNotEquals(dummyIdA, dummyIdB);
    }

    @Test
    void shouldBeDistinctOneIsAnotherClassId () {
        DummyId dummyIdA = new DummyId("123");
        AnotherDummyId dummyIdB = new AnotherDummyId("123");

        assertNotEquals(dummyIdA, dummyIdB);
        assertEquals(dummyIdA.hashCode(), dummyIdB.hashCode());
    }

}
