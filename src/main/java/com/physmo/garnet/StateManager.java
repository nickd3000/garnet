package com.physmo.garnet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StateManager {

    private GameState activeState;
    private GameState targetState;

    private Map<String, GameState> gameStates;
    private Garnet garnet;

    public StateManager(Garnet garnet) {
        this.garnet = garnet;

        gameStates = new HashMap<>();
    }

    public void update() {
        handleStateChange();
    }

    public Optional<GameState> getActiveState() {
        return Optional.ofNullable(activeState);
    }

    public void addState(String name, GameState state) {
        if (activeState==null) activeState = state;
        gameStates.put(name, state);
    }

    public void switchActiveState(String name) {
        for (String state : gameStates.keySet()) {
            if (state.equalsIgnoreCase(name)) {
                targetState = gameStates.get(name);
            }
        }
        // TODO: exception if not found?
    }

    public void handleStateChange() {
        if (targetState!=null) {
            targetState._init(garnet);
            activeState=targetState;
            targetState=null;
        }
    }
}
