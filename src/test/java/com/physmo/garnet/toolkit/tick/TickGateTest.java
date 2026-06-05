package com.physmo.garnet.toolkit.tick;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TickGateTest {

    @Test
    void blocksBeforeIntervalAndAllowsAtInterval() {
        TickGate gate = new TickGate(1.0);

        assertFalse(gate.allow(0.25));
        assertEquals(0.75, gate.getTimeUntilNextAllow());

        assertTrue(gate.allow(0.75));
        assertEquals(1.0, gate.getTimeUntilNextAllow());
    }

    @Test
    void carriesSurplusTimeForward() {
        TickGate gate = new TickGate(1.0);

        assertTrue(gate.allow(1.25));

        assertEquals(0.75, gate.getTimeUntilNextAllow());
    }

    @Test
    void largeDeltaAllowsOncePerCall() {
        TickGate gate = new TickGate(1.0);

        assertTrue(gate.allow(3.0));
        assertTrue(gate.allow(0.0));
        assertTrue(gate.allow(0.0));
        assertFalse(gate.allow(0.0));
    }

    @Test
    void resetClearsAccumulatedTime() {
        TickGate gate = new TickGate(1.0);
        gate.allow(0.5);

        gate.reset();

        assertEquals(1.0, gate.getTimeUntilNextAllow());
        assertFalse(gate.allow(0.5));
    }

    @Test
    void rejectsInvalidIntervalAndDelta() {
        assertThrows(IllegalArgumentException.class, () -> new TickGate(0.0));
        assertThrows(IllegalArgumentException.class, () -> new TickGate(-1.0));

        TickGate gate = new TickGate(1.0);
        assertThrows(IllegalArgumentException.class, () -> gate.allow(-1.0));
    }
}
