package com.physmo.garnet.toolkit.tick;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CooldownTest {

    @Test
    void newCooldownIsReady() {
        Cooldown cooldown = new Cooldown(1.0);

        assertTrue(cooldown.isReady());
        assertEquals(0.0, cooldown.getRemaining());
        assertEquals(1.0, cooldown.getProgress());
    }

    @Test
    void tryUseSucceedsOnceThenWaitsForElapsedDuration() {
        Cooldown cooldown = new Cooldown(1.0);

        assertTrue(cooldown.tryUse());
        assertFalse(cooldown.tryUse());
        assertEquals(1.0, cooldown.getRemaining());

        cooldown.tick(0.25);

        assertFalse(cooldown.isReady());
        assertEquals(0.25, cooldown.getProgress());

        cooldown.tick(0.75);

        assertTrue(cooldown.isReady());
        assertTrue(cooldown.tryUse());
    }

    @Test
    void useForcesCooldownActive() {
        Cooldown cooldown = new Cooldown(1.0);

        cooldown.use();

        assertFalse(cooldown.isReady());
        assertEquals(1.0, cooldown.getRemaining());
    }

    @Test
    void resetRestoresReadyState() {
        Cooldown cooldown = new Cooldown(1.0);
        cooldown.use();

        cooldown.reset();

        assertTrue(cooldown.isReady());
        assertEquals(0.0, cooldown.getRemaining());
    }

    @Test
    void rejectsNegativeDurationAndDelta() {
        assertThrows(IllegalArgumentException.class, () -> new Cooldown(-1.0));

        Cooldown cooldown = new Cooldown(1.0);
        assertThrows(IllegalArgumentException.class, () -> cooldown.tick(-1.0));
    }
}
