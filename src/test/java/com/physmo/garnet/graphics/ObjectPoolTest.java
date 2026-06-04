package com.physmo.garnet.graphics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ObjectPoolTest {

    private int nextId = 1;

    @Test
    void getFreeObjectReusesReleasedObjectsInLifoOrder() {
        ObjectPool<String> objectPool = new ObjectPool<>(
                String.class,
                () -> "hello " + nextId++);

        String s1 = objectPool.getFreeObject();
        String s2 = objectPool.getFreeObject();

        objectPool.releaseObject(s1);
        objectPool.releaseObject(s2);

        assertEquals("hello 2", objectPool.getFreeObject());
        assertSame(s1, objectPool.getFreeObject());
    }

    @Test
    void releaseObjectExpandsPoolWhenCapacityIsReached() {
        ObjectPool<String> objectPool = new ObjectPool<>(
                String.class,
                () -> "hello " + nextId++);

        for (int i = 0; i < 50; i++) {
            objectPool.releaseObject("value-" + i);
        }

        assertEquals(100, objectPool.pool.length);
    }
}
