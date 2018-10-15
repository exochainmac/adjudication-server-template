package com.exochain.api.bc.fsm.authsucceeded;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ContextEvent;

public class AuthSucceededEvent extends ContextEvent {
    public static final String EVENT_NAME = "authSucceededEvent";
    public AuthSucceededEvent(BcLoginContext ctx) {
        super(EVENT_NAME, ctx);
    }
}
