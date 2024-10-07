package com.physmo.garnet.toolkit

import com.physmo.garnet.toolkit.stateMachine.StateMachine
import spock.lang.Specification

class StateMachineTest extends Specification {

    int stateCounter = 0

    def "state machine transitions from state1 to state2 after three ticks"() {
        given: "A state machine with two states"
            List<String> debugStrings = new ArrayList<>()
            StateMachine stateMachine = new StateMachine()

            stateMachine.addState("state1", { t ->
                stateCounter++
                debugStrings.add("a")
                if (stateCounter > 2) stateMachine.changeState("state2")
            })

            stateMachine.addState("state2", { t ->
                debugStrings.add("b")
            })

        when: "The state machine is ticked 5 times"
            for (int i = 0; i < 5; i++) {
                stateMachine.tick(1)
            }

        then: "The debugStrings list should reflect the transitions and actions"
            debugStrings[0] == "a"
            debugStrings[1] == "a"
            debugStrings[2] == "a"
            debugStrings[3] == "b"
            debugStrings[4] == "b"
    }


    def "test state transitions"() {
        given: "A state machine with two states"
            int stateTick1 = 0
            int stateTick2 = 0
            int transitionTick = 0
            List<String> debugStrings = new ArrayList<>()
            StateMachine stateMachine = new StateMachine()

            stateMachine.addState("state1", { t ->
                stateTick1++
                if (stateTick1 > 2) stateMachine.changeState("state2")
            })

            stateMachine.addState("state2", { t ->
                stateTick2++
            })

            stateMachine.addTransition("state1", "state2", { t ->
                transitionTick++
            })

        when: "The state machine is ticked 5 times"
            for (int i = 0; i < 5; i++) {
                stateMachine.tick(1)
            }

        then: "The number of ticks are correct for each state"
            stateMachine.getCurrentStateName() == "state2"
            stateMachine.isCurrentState(stateMachine.getCurrentStateName())
            stateTick1 == 3
            stateTick2 == 2

        and: "The transition is ticked only once"
            transitionTick == 1
    }
}