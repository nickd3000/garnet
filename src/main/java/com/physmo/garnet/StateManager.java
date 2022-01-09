package com.physmo.garnet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StateManager {

    private GameState activeState;
    private GameState targetState;
    private List<GameState> activeSubStates;

    private List<String> subStatePushRequests;
    private List<String> subStatePopRequests;

    private Map<String, GameState> gameStates;
    private Garnet garnet;

    public StateManager(Garnet garnet) {
        this.garnet = garnet;
        gameStates = new HashMap<>();
        activeSubStates = new ArrayList<>();
        subStatePushRequests = new ArrayList<>();
        subStatePopRequests = new ArrayList<>();
    }

    // called after gamestate ticks so safe to add/remove/change states here.
    public void update() {

        handleStateChange();
        handleSubstatePop();
        handleSubstatePush();
    }

    public void handleStateChange() {
        if (targetState != null) {
            targetState._init(garnet);
            activeState = targetState;
            targetState = null;
            garnet.getInput().postStateChangeTask();
        }


    }

    public void handleSubstatePush() {
        for (String substateName : subStatePushRequests) {

            GameState state = gameStates.get(substateName);
            state._init(garnet);
            if (state == null) {
                System.out.println("Substate not found: " + substateName);
                return;
            }
            activeSubStates.add(state);
            garnet.getInput().postStateChangeTask();
        }

        subStatePushRequests.clear();
    }

    public void handleSubstatePop() {
        List<GameState> newActiveSubStates = new ArrayList<>();
        boolean substateRemoved = false;
        for (GameState activeSubState : activeSubStates) {
            boolean skip = false;
            for (String subStatePopRequest : subStatePopRequests) {
                if (activeSubState.getName().equalsIgnoreCase(subStatePopRequest)) {
                    skip = true;
                    substateRemoved = true;
                    System.out.println("remove");
                }
            }

            if (!skip) {
                newActiveSubStates.add(activeSubState);
            }
        }
        subStatePopRequests.clear();
        activeSubStates = newActiveSubStates;
        if (substateRemoved) {
            garnet.getInput().postStateChangeTask();
        }
    }

    public void tick(double delta) {
        // Only tick main state if there are no active sub states.
        if (activeSubStates.size() == 0) activeState._tick(delta);

        // Find the last active substate and tick it.
        if (activeSubStates.size() > 0)
            activeSubStates.get(activeSubStates.size() - 1)._tick(delta);
    }

    public void draw() {
        activeState._draw();
        for (GameState activeSubState : activeSubStates) {
            activeSubState._draw();
        }

    }

    public Optional<GameState> getActiveState() {
        return Optional.ofNullable(activeState);
    }

    public void addState(String name, GameState state) {
        if (activeState == null) activeState = state;
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

    public void pushSubState(String name) {
        subStatePushRequests.add(name);


    }

    public void popSubState(String name) {
        subStatePopRequests.add(name);
    }
}
