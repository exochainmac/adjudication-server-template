package com.exochain.api.bc.fsm.rawreceived;

import com.exochain.api.bc.RMReg;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.bypasserror.BypassErrorEvent;
import com.exochain.api.bc.fsm.ExecutionState;
import com.exochain.api.bc.fsm.invalidinteractionerror.InvalidInteractionErrorEvent;
import com.exochain.api.bc.fsm.claimsparsed.JwtClaimsParsedEvent;
import com.exochain.jwt.ExoJwtException;
import com.exochain.jwt.JwtRMReg;
import com.exochain.result.meta.WarnLoggedResultMeta;
import com.nimbusds.jwt.JWTClaimsSet;
import org.jeasy.states.api.Event;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * State that processes the raw token by decoding it
 */
public class RawTokenReceivedState extends ExecutionState {
    private static final XLogger L = XLoggerFactory.getXLogger(RawTokenReceivedState.class);

    public RawTokenReceivedState() {
        super("rawTokenReceivedState");
    }

    @Override
    public Event stateAction(BcLoginContext ctx) {
        L.entry(ctx);

        Event resultEvent;
        try {
            // Parse and validate the raw token
            JWTClaimsSet claimsSet = ctx.getJwtTokenProcessor().process(ctx.getRawInputToken(), null);
            resultEvent = new JwtClaimsParsedEvent(ctx, claimsSet);
        } catch(ExoJwtException e) {
            // Assume someone sent some crap
            resultEvent = new InvalidInteractionErrorEvent(ctx, e);
        } catch (Exception e) {
            resultEvent = new BypassErrorEvent(ctx, new WarnLoggedResultMeta(RMReg.UNEXPECTED_INTERNAL_ERROR, e));
        }
        return resultEvent;
    }
}
