package com.physmo.garnet.toolkit.stateMachine;

@FunctionalInterface
public interface StateMachineState {
    void tick(double t);
}
