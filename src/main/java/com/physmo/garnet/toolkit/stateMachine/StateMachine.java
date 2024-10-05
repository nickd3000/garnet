package com.physmo.garnet.toolkit.stateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StateMachine {

    public static final String ANY_STATE = "ANY_STATE";
    Map<String, StateMachineState> stateMap = new HashMap<>();
    String targetStateName = null;
    StateMachineState currentState;
    String currentStateName;
    List<Transition> transitions = new ArrayList<>();

    /**
     * Get a string representing the current states name.
     *
     * @return
     */
    public String getCurrentStateName() {
        return (currentStateName == null ? "" : currentStateName);
    }

    public boolean isCurrentState(String name) {
        return currentStateName.compareToIgnoreCase(name) == 0;
    }

    /**
     * Add a state to the list of states this machine can use.
     *
     * @param stateName The string identifier that the state will be referred to by
     * @param state     state object
     */
    public void addState(String stateName, StateMachineState state) {
        stateMap.put(stateName, state);
        if (currentState == null) {
            currentState = state;
            currentStateName = stateName;
        }
    }

    /**
     * Add a state that will be called when moving between two specified states.
     * The supplied state will be called ticked once, just before the target state.
     * The intended use is to perform cleanup before switching to the next state
     *
     * @param fromState
     * @param toState
     * @param state
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
     * Handle any state changes then tick the currently active state.
     *
     * @param delta
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
