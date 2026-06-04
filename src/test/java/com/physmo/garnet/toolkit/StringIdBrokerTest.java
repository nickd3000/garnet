package com.physmo.garnet.toolkit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringIdBrokerTest {

    @ParameterizedTest
    @NullAndEmptySource
    void getIdRejectsNullOrEmptyKeys(String key) {
        assertThrows(IllegalArgumentException.class, () -> StringIdBroker.getInstance().getId(key));
    }

    @Test
    void getIdReturnsSameIdForSameKey() {
        String key = "sampleKey";

        int id1 = StringIdBroker.getInstance().getId(key);
        int id2 = StringIdBroker.getInstance().getId(key);

        assertEquals(id1, id2);
    }

    @Test
    void getIdReturnsDifferentIdsForDifferentKeys() {
        int id1 = StringIdBroker.getInstance().getId("sampleKey1");
        int id2 = StringIdBroker.getInstance().getId("sampleKey2");

        assertNotEquals(id1, id2);
    }
}
