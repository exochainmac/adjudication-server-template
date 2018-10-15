package com.exochain.api.bc.fsm.emailvalidation;

import com.exochain.api.bc.domain.AuthenticationFactor;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ContextEvent;
import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkNotNull;

public class EmailValidationEvent extends ContextEvent {
    private final AuthenticationFactor authenticationFactor;

    public EmailValidationEvent(BcLoginContext ctx, AuthenticationFactor emailFactor) {
        super(EmailValidationEvent.class.getSimpleName(), ctx);
        this.authenticationFactor = checkNotNull(emailFactor);
    }

    public AuthenticationFactor getAuthenticationFactor() {
        return authenticationFactor;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("authenticationFactor", authenticationFactor)
                .add("name", name)
                .add("timestamp", timestamp)
                .toString();
    }
}
