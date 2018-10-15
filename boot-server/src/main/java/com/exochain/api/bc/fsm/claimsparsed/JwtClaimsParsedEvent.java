package com.exochain.api.bc.fsm.claimsparsed;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ContextEvent;
import com.google.common.base.MoreObjects;
import com.nimbusds.jwt.JWTClaimsSet;

public class JwtClaimsParsedEvent extends ContextEvent {
    protected final JWTClaimsSet jwtClaimsSet;

    public JwtClaimsParsedEvent(BcLoginContext ctx, JWTClaimsSet claimsSet) {
        super("jwtClaimsParsedEvent", ctx);
        this.jwtClaimsSet = claimsSet;
    }

    public JWTClaimsSet getJwtClaimsSet() {
        return jwtClaimsSet;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("jwtClaimsSet", jwtClaimsSet)
                .add("loginContext", loginContext)
                .add("name", name)
                .add("timestamp", timestamp)
                .toString();
    }
}
