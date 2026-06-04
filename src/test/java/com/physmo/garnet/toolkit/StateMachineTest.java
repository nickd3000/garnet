package com.physmo.garnet.toolkit;

import com.physmo.garnet.toolkit.stateMachine.StateMachine;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StateMachineTest {

    @Test
    void stateMachineTransitionsFromState1ToState2AfterThreeTicks() {
        List<String> debugStrings = new ArrayList<>();
        AtomicInteger stateCounter = new AtomicInteger();
        StateMachine stateMachine = new StateMachine();

        stateMachine.addState("state1", t -> {
            stateCounter.incrementAndGet();
            debugStrings.add("a");
            if (stateCounter.get() > 2) {
                stateMachine.changeState("state2");
            }
        });
        stateMachine.addState("state2", t -> debugStrings.add("b"));

        tick(stateMachine, 5);

        assertEquals(List.of("a", "a", "a", "b", "b"), debugStrings);
    }

    private static void tick(StateMachine stateMachine, int count) {
        for (int i = 0; i < count; i++) {
            stateMachine.tick(1);
        }
    }

    @Test
    void transitionStateTicksOnceWhenChangingState() {
        AtomicInteger stateTick1 = new AtomicInteger();
        AtomicInteger stateTick2 = new AtomicInteger();
        AtomicInteger transitionTick = new AtomicInteger();
        StateMachine stateMachine = new StateMachine();

        stateMachine.addState("state1", t -> {
            stateTick1.incrementAndGet();
            if (stateTick1.get() > 2) {
                stateMachine.changeState("state2");
            }
        });
        stateMachine.addState("state2", t -> stateTick2.incrementAndGet());
        stateMachine.addTransition("state1", "state2", t -> transitionTick.incrementAndGet());

        tick(stateMachine, 5);

        assertEquals("state2", stateMachine.getCurrentStateName());
        assertTrue(stateMachine.isCurrentState(stateMachine.getCurrentStateName()));
        assertEquals(3, stateTick1.get());
        assertEquals(2, stateTick2.get());
        assertEquals(1, transitionTick.get());
    }
}
