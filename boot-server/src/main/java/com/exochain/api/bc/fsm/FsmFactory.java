package com.exochain.api.bc.fsm;

import org.jeasy.states.api.FiniteStateMachine;

public interface FsmFactory {
    FiniteStateMachine createFsm();
}
