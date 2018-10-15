package com.exochain.api.bc.fsm.emailvalidated;

import com.exochain.api.bc.domain.AuthenticationFactor;
import com.exochain.api.bc.fsm.BaseEventHandler;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.claimsparsed.JwtClaimsParsedEvent;
import com.nimbusds.jwt.JWTClaimsSet;
import org.jeasy.states.api.Event;

/**
 * Takes the raw token fromt he event and puts it in the login context
 */
public class EmailValidatedHandler extends BaseEventHandler {
    @Override
    public void handleEvent(final Event incomingEvent) throws Exception {
        if (incomingEvent instanceof EmailValidatedEvent) {
            EmailValidatedEvent evt = (EmailValidatedEvent) incomingEvent;
            BcLoginContext ctx = evt.getLoginContext();
            AuthenticationFactor oldFactor = ctx.getAccount().getEmailFactor();
            AuthenticationFactor newFactor = new AuthenticationFactor(oldFactor.getType(), oldFactor.getOtpAddress(), true);
            ctx.getAccount().setEmailFactor(newFactor);
        } else {
            throwInvalidEvent(incomingEvent);
        }
    }
}
