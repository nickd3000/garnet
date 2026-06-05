package com.physmo.garnet.toolkit.tick;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TickableTest {

    @Test
    void addToPoolRegistersObjectAndReturnsConcreteType() {
        TickPool tickPool = new TickPool();

        Timer timer = new Timer().addToPool(tickPool);

        assertSame(timer, tickPool.tickables.get(0));
    }

    @Test
    void addToPoolRejectsNullPool() {
        Timer timer = new Timer();

        assertThrows(IllegalArgumentException.class, () -> timer.addToPool(null));
    }
}
