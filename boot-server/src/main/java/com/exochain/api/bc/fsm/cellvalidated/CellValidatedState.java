package com.exochain.api.bc.fsm.cellvalidated;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ExecutionState;
import com.exochain.api.bc.fsm.acctfound.AccountFoundEvent;
import org.jeasy.states.api.Event;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Saves the users account after they have completed validate
 */
public class CellValidatedState extends ExecutionState {
    private static final XLogger L = XLoggerFactory.getXLogger(CellValidatedState.class);

    public CellValidatedState() {
        super("cellValidatedState");
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Event stateAction(BcLoginContext ctx) {
        L.entry();

        Event resultEvent = null;

        // Cell was validated so update the database
        ctx.getRegistrar().update(ctx.getAccount());
        resultEvent = new AccountFoundEvent(ctx, ctx.getAccount());

        return resultEvent;
    }
}
