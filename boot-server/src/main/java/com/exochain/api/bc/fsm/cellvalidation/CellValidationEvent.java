package com.exochain.api.bc.fsm.cellvalidation;

import com.exochain.api.bc.domain.AuthenticationFactor;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ContextEvent;
import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkNotNull;

public class CellValidationEvent extends ContextEvent {
    private final AuthenticationFactor authenticationFactor;

    public CellValidationEvent(BcLoginContext ctx, AuthenticationFactor cellFactor) {
        super(CellValidationEvent.class.getSimpleName(), ctx);
        this.authenticationFactor = checkNotNull(cellFactor);
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
