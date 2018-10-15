package com.exochain.api.bc.fsm.authsucceeded;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ExecutionState;
import com.exochain.api.bc.fsm.resultbuilding.ResultBuildingEvent;
import org.jeasy.states.api.Event;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class AuthSucceededState extends ExecutionState {
    public static final String STATE_NAME = "accountValidatedState";
    private static final XLogger L = XLoggerFactory.getXLogger(AuthSucceededState.class);


    public AuthSucceededState() {
        super(STATE_NAME);
    }

    @Override
    public Event stateAction(BcLoginContext ctx) {
        L.entry();
        Event resultEvent;

        // Pass through successfully - nothing to do here yet
        resultEvent = new ResultBuildingEvent(ctx);
        return resultEvent;
    }
}
