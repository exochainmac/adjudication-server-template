package com.exochain.api.bc.fsm.acctfound;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ExecutionState;
import com.exochain.api.bc.fsm.acctevaluation.AccountEvaluationEvent;
import org.jeasy.states.api.Event;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class AccountFoundState extends ExecutionState {
    public static final String STATE_NAME = "accountFoundState";
    private static final XLogger L = XLoggerFactory.getXLogger(AccountFoundState.class);


    public AccountFoundState() {
        super(STATE_NAME);
    }

    @Override
    public Event stateAction(BcLoginContext ctx) {
        L.entry();

        // Nothing to do for now except evaluate
        return L.exit(new AccountEvaluationEvent(ctx, ctx.getAccount()));
    }
}
