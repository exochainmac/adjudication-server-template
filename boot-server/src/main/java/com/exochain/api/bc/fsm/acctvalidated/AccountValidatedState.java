package com.exochain.api.bc.fsm.acctvalidated;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ExecutionState;
import com.exochain.api.bc.fsm.authsucceeded.AuthSucceededEvent;
import com.exochain.api.bc.fsm.resultbuilding.ResultBuildingEvent;
import org.jeasy.states.api.Event;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class AccountValidatedState extends ExecutionState {
    public static final String STATE_NAME = "accountValidatedState";
    private static final XLogger L = XLoggerFactory.getXLogger(AccountValidatedState.class);

    public AccountValidatedState() {
        super(STATE_NAME);
    }

    @Override
    public Event stateAction(BcLoginContext ctx) {
        L.entry();

        Event resultEvent;

        //TODO: Exception handling needed
        if(ctx.isNewAccount()) {
            ctx.getRegistrar().create(ctx.getAccount());
            ctx.setNewAccount(false);
        }

        if(ctx.isNewChannelAccount()) {
            ctx.getRegistrar().addAccountToChannel(ctx.getChannelId(), ctx.getChannelUserId(), ctx.getAccount());
            ctx.setNewChannelAccount(false);
        }

        if(ctx.isApersonaAuthPassed()) {
            // User has already performed a live aPersona auth in this session
            resultEvent = new ResultBuildingEvent(ctx);
        } else {
            //resultEvent = new ApersonaCheckEvent(ctx);
            throw new IllegalStateException("APERSONA CHECK HAS NOT BEEN IMPLEMENTED YET");
        }
        // Account details have been validated by the user so check aPersona to determine if they
        // need to auth via a factor, and if so, send to factor selection screen

        // Pass through for now

        resultEvent = new AuthSucceededEvent(ctx);
        return resultEvent;
    }
}
