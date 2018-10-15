package com.exochain.api.bc.fsm.emailvalidated;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ContextEvent;
import com.google.common.base.MoreObjects;
import com.nimbusds.jwt.JWTClaimsSet;

public class EmailValidatedEvent extends ContextEvent {
    public EmailValidatedEvent(BcLoginContext ctx) {
        super("emailValidatedEvent", ctx);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("timestamp", timestamp)
                .toString();
    }
}
