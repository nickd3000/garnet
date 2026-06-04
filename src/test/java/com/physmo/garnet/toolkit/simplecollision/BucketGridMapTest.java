package com.physmo.garnet.toolkit.simplecollision;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BucketGridMapTest {

    @Test
    void putStoresManyDistinctKeys() {
        BucketGridMap bucketGridMap = new BucketGridMap();
        List<Object> objects = new ArrayList<>();

        for (int i = 0; i < 5000; i++) {
            bucketGridMap.put(i, objects);
        }

        assertEquals(5000, bucketGridMap.size());
    }
}
