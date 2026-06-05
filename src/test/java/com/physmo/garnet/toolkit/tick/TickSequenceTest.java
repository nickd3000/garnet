package com.physmo.garnet.toolkit.tick;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TickSequenceTest {

    @Test
    void executesActionsInOrder() {
        List<String> events = new ArrayList<>();
        TickSequence sequence = new TickSequence()
                .then(() -> events.add("a"))
                .then(() -> events.add("b"));

        sequence.start();
        sequence.tick(0.0);

        assertEquals(List.of("a", "b"), events);
        assertTrue(sequence.isComplete());
        assertFalse(sequence.isRunning());
    }

    @Test
    void waitStepsConsumeTimeDeterministically() {
        List<String> events = new ArrayList<>();
        TickSequence sequence = new TickSequence()
                .waitFor(1.0)
                .then(() -> events.add("after"));

        sequence.start();
        sequence.tick(0.25);

        assertEquals(List.of(), events);
        assertTrue(sequence.isRunning());

        sequence.tick(0.75);

        assertEquals(List.of("after"), events);
        assertTrue(sequence.isComplete());
    }

    @Test
    void largeDeltaProgressesAcrossMultipleSteps() {
        List<String> events = new ArrayList<>();
        TickSequence sequence = new TickSequence()
                .waitFor(0.5)
                .then(() -> events.add("a"))
                .waitFor(0.5)
                .then(() -> events.add("b"));

        sequence.start();
        sequence.tick(1.0);

        assertEquals(List.of("a", "b"), events);
        assertTrue(sequence.isComplete());
    }

    @Test
    void loopingSequenceRestartsAndCarriesSurplusDelta() {
        List<String> events = new ArrayList<>();
        TickSequence sequence = new TickSequence()
                .then(() -> events.add("tick"))
                .waitFor(1.0)
                .loop();

        sequence.start();
        sequence.tick(2.5);

        assertEquals(List.of("tick", "tick", "tick"), events);
        assertTrue(sequence.isRunning());
        assertFalse(sequence.isComplete());
        assertTrue(sequence.isLooping());
    }

    @Test
    void stopPausesLoopingSequenceWithoutClearingLoopBehavior() {
        List<String> events = new ArrayList<>();
        TickSequence sequence = new TickSequence()
                .waitFor(1.0)
                .then(() -> events.add("run"))
                .loop();

        sequence.start();
        sequence.stop();
        sequence.tick(1.0);

        assertEquals(List.of(), events);
        assertFalse(sequence.isRunning());
        assertTrue(sequence.isLooping());
    }

    @Test
    void resetRewindsAndKeepsLoopBehavior() {
        List<String> events = new ArrayList<>();
        TickSequence sequence = new TickSequence()
                .waitFor(1.0)
                .then(() -> events.add("run"))
                .loop();

        sequence.start();
        sequence.tick(0.5);
        sequence.reset();
        sequence.start();
        sequence.tick(0.5);

        assertEquals(List.of(), events);
        assertTrue(sequence.isLooping());

        sequence.tick(0.5);

        assertEquals(List.of("run"), events);
    }

    @Test
    void resetReturnsNonLoopingSequenceToBeginning() {
        List<String> events = new ArrayList<>();
        TickSequence sequence = new TickSequence()
                .then(() -> events.add("run"));

        sequence.start();
        sequence.tick(0.0);
        sequence.reset();
        sequence.start();
        sequence.tick(0.0);

        assertEquals(List.of("run", "run"), events);
    }

    @Test
    void stopPreventsFurtherProgress() {
        List<String> events = new ArrayList<>();
        TickSequence sequence = new TickSequence()
                .waitFor(1.0)
                .then(() -> events.add("run"));

        sequence.start();
        sequence.stop();
        sequence.tick(1.0);

        assertEquals(List.of(), events);
        assertFalse(sequence.isRunning());
        assertFalse(sequence.isComplete());
    }

    @Test
    void emptyOrZeroTimeOnlyLoopsAreRejected() {
        assertThrows(IllegalStateException.class, () -> new TickSequence().loop());
        assertThrows(IllegalStateException.class, () -> new TickSequence().then(() -> {
        }).loop());
        assertThrows(IllegalStateException.class, () -> new TickSequence().waitFor(0.0).loop());
    }

    @Test
    void rejectsInvalidInputs() {
        TickSequence sequence = new TickSequence();

        assertThrows(IllegalArgumentException.class, () -> sequence.waitFor(-1.0));
        assertThrows(IllegalArgumentException.class, () -> sequence.then(null));

        sequence.waitFor(1.0).start();
        assertThrows(IllegalArgumentException.class, () -> sequence.tick(-1.0));
    }
}
