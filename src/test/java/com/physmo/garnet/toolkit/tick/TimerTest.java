package com.physmo.garnet.toolkit.tick;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimerTest {

    @Test
    void startsTicksAndCompletes() {
        Timer timer = new Timer();

        timer.start(1.0);
        timer.tick(0.25);

        assertTrue(timer.isRunning());
        assertFalse(timer.isComplete());
        assertEquals(0.25, timer.getElapsed());
        assertEquals(0.75, timer.getRemaining());
        assertEquals(0.25, timer.getProgress());

        timer.tick(0.75);

        assertFalse(timer.isRunning());
        assertTrue(timer.isComplete());
        assertEquals(1.0, timer.getElapsed());
        assertEquals(0.0, timer.getRemaining());
        assertEquals(1.0, timer.getProgress());
    }

    @Test
    void stopFreezesElapsedTime() {
        Timer timer = new Timer();
        timer.start(1.0);
        timer.tick(0.25);

        timer.stop();
        timer.tick(0.5);

        assertFalse(timer.isRunning());
        assertEquals(0.25, timer.getElapsed());
    }

    @Test
    void resetClearsElapsedAndProgress() {
        Timer timer = new Timer();
        timer.start(1.0);
        timer.tick(0.5);

        timer.reset();

        assertFalse(timer.isRunning());
        assertFalse(timer.isComplete());
        assertEquals(0.0, timer.getElapsed());
        assertEquals(0.0, timer.getProgress());
    }

    @Test
    void largeDeltaCompletesWithoutNegativeRemaining() {
        Timer timer = new Timer();

        timer.start(1.0);
        timer.tick(3.0);

        assertTrue(timer.isComplete());
        assertEquals(1.0, timer.getElapsed());
        assertEquals(0.0, timer.getRemaining());
    }

    @Test
    void restartReusesDuration() {
        Timer timer = new Timer(2.0);

        timer.restart();
        timer.tick(1.0);

        assertTrue(timer.isRunning());
        assertEquals(2.0, timer.getDuration());
        assertEquals(1.0, timer.getElapsed());
    }

    @Test
    void zeroDurationCompletesImmediately() {
        Timer timer = new Timer();

        timer.start(0.0);

        assertFalse(timer.isRunning());
        assertTrue(timer.isComplete());
        assertEquals(1.0, timer.getProgress());
    }

    @Test
    void rejectsNegativeDurationAndDelta() {
        assertThrows(IllegalArgumentException.class, () -> new Timer(-1.0));

        Timer timer = new Timer();
        assertThrows(IllegalArgumentException.class, () -> timer.start(-1.0));
        assertThrows(IllegalArgumentException.class, () -> timer.tick(-1.0));
    }
}
