package com.physmo.garnet.toolkit.stateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * A simple finite state machine that manages a set of named states and transitions between them.
 *
 * <p>States are represented by {@link StateMachineState} objects and are identified by string names.
 * The machine ticks the currently active state each frame via {@link #tick(double)}.
 * State changes requested via {@link #changeState(String)} are deferred and applied at the start
 * of the next tick, allowing optional {@link Transition} states to run cleanup logic between states.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * StateMachine sm = new StateMachine();
 * sm.addState("idle", new IdleState());
 * sm.addState("running", new RunningState());
 * sm.addTransition(StateMachine.ANY_STATE, "running", new CleanupState());
 * sm.changeState("running");
 * sm.tick(deltaTime);
 * }</pre>
 *
 * @see StateMachineState
 * @see Transition
 */
public class StateMachine {

    public static final String ANY_STATE = "ANY_STATE";
    Map<String, StateMachineState> stateMap = new HashMap<>();
    String targetStateName = null;
    StateMachineState currentState;
    String currentStateName;
    List<Transition> transitions = new ArrayList<>();

    /**
     * Get a string representing the current state's name.
     *
     * @return the name of the current state, or an empty string if no state has been set
     */
    public String getCurrentStateName() {
        return (currentStateName == null ? "" : currentStateName);
    }

    /**
     * Check whether the current state matches the given name (case-insensitive).
     *
     * @param name the state name to compare against
     * @return {@code true} if the current state name matches {@code name}, {@code false} otherwise
     */
    public boolean isCurrentState(String name) {
        return currentStateName.compareToIgnoreCase(name) == 0;
    }

    /**
     * Add a state to the list of states this machine can use.
     *
     * @param stateName The string identifier that the state will be referred to by
     * @param state     the StateMachineState implementation to register
     */
    public void addState(String stateName, StateMachineState state) {
        stateMap.put(stateName, state);
        if (currentState == null) {
            currentState = state;
            currentStateName = stateName;
        }
    }

    /**
     * Add a transition state that is ticked once when moving between two specified states.
     * The transition state is ticked immediately before the machine switches to the target state.
     * The intended use is to perform cleanup or setup logic between state changes.
     * Use {@link #ANY_STATE} as {@code fromState} or {@code toState} to match any state.
     *
     * @param fromState the name of the source state, or {@link #ANY_STATE} to match any source state
     * @param toState   the name of the target state, or {@link #ANY_STATE} to match any target state
     * @param state     the transition state to tick when this transition is triggered
     */
    public void addTransition(String fromState, String toState, StateMachineState state) {
        Transition transition = new Transition();
        transition.fromState = fromState;
        transition.toState = toState;
        transition.transitionState = state;
        transitions.add(transition);
    }

    /**
     * Request that the state is changed on the next tick.
     *
     * @param stateName Name of the target state
     */
    public void changeState(String stateName) {
        targetStateName = stateName;
    }

    /**
     * Handle any pending state changes, then tick the currently active state.
     *
     * @param delta time elapsed since the last tick, in seconds
     */
    public void tick(double delta) {
        handleStateChange();

        if (currentState != null) {
            currentState.tick(delta);
        }
    }

    private void handleStateChange() {
        if (targetStateName == null) return;

        // First, run any state transition that matches our source and target state.
        Optional<Transition> transition = findTransition(currentStateName, targetStateName);
        transition.ifPresent(t -> t.transitionState.tick(0));

        if (stateMap.containsKey(targetStateName)) {
            currentState = stateMap.get(targetStateName);
            currentStateName = targetStateName;
            targetStateName = null;
        } else {
            // todo: exception if name not found
        }
    }


    private Optional<Transition> findTransition(String fromState, String toState) {
        for (Transition transition : transitions) {
            if (transition.fromState.compareTo(fromState) != 0 && transition.fromState.compareTo(ANY_STATE) != 0)
                continue;
            if (transition.toState.compareTo(toState) != 0 && transition.toState.compareTo(ANY_STATE) != 0) continue;
            return Optional.of(transition);
        }
        return Optional.empty();
    }
}
