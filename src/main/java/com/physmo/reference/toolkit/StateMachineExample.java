package com.physmo.reference.toolkit;

import com.physmo.garnet.toolkit.stateMachine.StateMachine;

public class StateMachineExample {

    static double timer = 0;

    public static void main(String[] args) {

        StateMachine stateMachine = new StateMachine();


        stateMachine.addState("one", t -> {
            System.out.println("State one ticked - timer=" + timer);
            timer += t;
            if (timer > 2) stateMachine.changeState("two");

        });

        stateMachine.addState("two", t -> {
            System.out.println("State two ticked - timer=" + timer);
            timer += t;
        });

        stateMachine.addTransition("one", "two", t -> {
            System.out.println("State transition");
        });


        for (int i = 0; i < 5; i++) {
            stateMachine.tick(1);
        }


    }
}
