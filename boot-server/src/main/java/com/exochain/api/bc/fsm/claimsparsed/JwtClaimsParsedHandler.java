package com.exochain.api.bc.fsm.claimsparsed;

import com.exochain.api.bc.fsm.BaseEventHandler;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.nimbusds.jwt.JWTClaimsSet;
import org.jeasy.states.api.Event;

/**
 * Takes the raw token fromt he event and puts it in the login context
 */
public class JwtClaimsParsedHandler extends BaseEventHandler {
    @Override
    public void handleEvent(final Event incomingEvent) throws Exception {
        if(incomingEvent instanceof JwtClaimsParsedEvent) {
            JwtClaimsParsedEvent evt = (JwtClaimsParsedEvent) incomingEvent;
            JWTClaimsSet claimsSet = evt.getJwtClaimsSet();
            BcLoginContext ctx = evt.getLoginContext();
            ctx.setJwtClaimsSet(claimsSet);
            ctx.setChannelUserId(claimsSet.getSubject());
        } else {
            throwInvalidEvent(incomingEvent);
        }
    }
}
