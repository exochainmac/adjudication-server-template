package com.exochain.api.bc.fsm;

import org.jeasy.states.api.Event;
import org.jeasy.states.api.State;

public abstract class ExecutionState extends State {

    public ExecutionState(String name) {
        super(name);
    }

    public abstract Event stateAction(BcLoginContext ctx);
}
