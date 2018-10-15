package com.exochain.api.bc.fsm.emailvalidated;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ExecutionState;
import com.exochain.api.bc.fsm.acctfound.AccountFoundEvent;
import org.jeasy.states.api.Event;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Attempts to load the user account data and go to next states of either user not found or claims verification
 */
public class EmailValidatedState extends ExecutionState {
    private static final XLogger L = XLoggerFactory.getXLogger(EmailValidatedState.class);

    public EmailValidatedState() {
        super("emailValidatedState");
    }

    @Override
    public Event stateAction(BcLoginContext ctx) {
        L.entry();

        Event resultEvent = null;

        // Email was validated so update the database
         ctx.getRegistrar().update(ctx.getAccount());
        resultEvent = new AccountFoundEvent(ctx, ctx.getAccount());

        return resultEvent;
    }
}
