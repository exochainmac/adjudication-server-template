package com.exochain.api.bc.fsm.acctvalidated;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ContextEvent;
import org.jeasy.states.api.Event;

public class AccountValidatedEvent extends ContextEvent {
    public static final String EVENT_NAME = "accountValidatedEvent";
    public AccountValidatedEvent(BcLoginContext ctx) {
        super(EVENT_NAME, ctx);
    }
}
