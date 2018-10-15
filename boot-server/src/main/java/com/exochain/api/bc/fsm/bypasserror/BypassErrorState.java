package com.exochain.api.bc.fsm.bypasserror;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ExecutionState;
import com.exochain.api.bc.fsm.resultbuilding.ResultBuildingEvent;
import com.exochain.api.bc.fsm.resultready.ResultReadyEvent;
import org.jeasy.states.api.Event;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class BypassErrorState extends ExecutionState {
    private static final XLogger L = XLoggerFactory.getXLogger(BypassErrorState.class);

    public BypassErrorState() {
        super("bypassErrorState");
    }

    @Override
    public Event stateAction(BcLoginContext ctx) {
        L.entry();
        L.debug("Executing bypassErrorState action");
        /*
        Get stuff ready for final assembly and send event to transition
        to ResultConstruction state.
         */
        return new ResultBuildingEvent(ctx);
    }
}
