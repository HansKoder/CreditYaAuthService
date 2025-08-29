package org.pragma.creditya.model.shared.model.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.shared.model.valueobject.BaseId;

import static org.junit.jupiter.api.Assertions.*;

public class BaseEntityTest {
    private static class DummyId extends BaseId<String> {
        protected DummyId(String value) {
            super(value);
        }
    }

    private static class DummyEntity extends BaseEntity<DummyId> {
        String name;

        public DummyEntity(String id, String name) {
            this.setId(new DummyId(id));
            this.name = name;
        }
    }

    private static class AnotherDummyEntity extends BaseEntity<DummyId> {
        String name;

        public AnotherDummyEntity(String id, String name) {
            this.setId(new DummyId(id));
            this.name = name;
        }
    }

    @Test
    @DisplayName("Should be same objects when the id are exactly equals")
    void shouldBeEqualsWhenTwoObjectsHasTheSameId () {
        DummyEntity dummyEntityA = new DummyEntity("107", "doe");
        DummyEntity dummyEntityB = new DummyEntity("107", "stark");

        assertEquals(dummyEntityA, dummyEntityB);
        assertEquals(dummyEntityA.hashCode(), dummyEntityB.hashCode());
    }

    @Test
    @DisplayName("Should be distinct when each ID has unique value")
    void shouldBeDistinctWhenIdsAreDistinct () {
        DummyEntity dummyEntityA = new DummyEntity("107", "doe");
        DummyEntity dummyEntityB = new DummyEntity("108", "stark");

        assertNotEquals(dummyEntityA, dummyEntityB);
        assertNotEquals(dummyEntityA.hashCode(), dummyEntityB.hashCode());
    }

    @Test
    @DisplayName("Should be distinct when one Id is null")
    void shouldBeDistinctWhenOneHasIdNull () {
        DummyEntity dummyEntityA = new DummyEntity(null, "doe");
        DummyEntity dummyEntityB = new DummyEntity("108", "stark");

        assertNotEquals(dummyEntityA, dummyEntityB);
    }

    @Test
    @DisplayName("Should be distinct when one Id is null")
    void shouldBeDistinctWhenOneEntityIsNull() {
        DummyEntity dummyEntityA = new DummyEntity("107", "doe");

        assertNotEquals(null, dummyEntityA);
    }

    @Test
    @DisplayName("Should be distinct when one Id is null")
    void shouldBeDistinctWhenBothEntityAreDistinct() {
        DummyEntity dummyEntityA = new DummyEntity("107", "doe");
        AnotherDummyEntity dummyEntityB = new AnotherDummyEntity("107", "stark");

        assertNotEquals(dummyEntityA, dummyEntityB);
    }

}
