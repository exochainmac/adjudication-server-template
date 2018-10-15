package com.exochain.api.bc.fsm.waitingraw;

import com.exochain.api.bc.RMReg;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ExecutionState;
import com.exochain.api.bc.fsm.invalidinteractionerror.InvalidInteractionErrorEvent;
import com.exochain.result.meta.InfoLoggedResultMeta;
import org.jeasy.states.api.Event;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class WaitingForRawTokenState extends ExecutionState {
    private static final XLogger L = XLoggerFactory.getXLogger(WaitingForRawTokenState.class);

    public WaitingForRawTokenState() {
        super("waitingForRawTokenState");
    }

    @Override
    public Event stateAction(BcLoginContext ctx) {
        L.error("fix me to cause an error if this state action ever gets trigger");
        return new InvalidInteractionErrorEvent(ctx, new InfoLoggedResultMeta(RMReg.ANON_TRAFFIC_NOT_ALLOWED_ERROR));
    }
}
