package com.exochain.api.bc.fsm.claimsparsed;

import com.exochain.api.bc.RMReg;
import com.exochain.api.bc.domain.Account;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ExecutionState;
import com.exochain.api.bc.fsm.acctfound.AccountFoundEvent;
import com.exochain.api.bc.fsm.acctnotfound.AccountNotFoundEvent;
import com.exochain.api.bc.fsm.invalidinteractionerror.InvalidInteractionErrorEvent;
import com.exochain.result.meta.WarnLoggedResultMeta;
import com.nimbusds.jwt.JWTClaimsSet;
import org.jeasy.states.api.Event;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Attempts to load the user account data and go to next states of either user not found or claims verification
 */
public class JwtClaimsParsedState extends ExecutionState {
    private static final XLogger L = XLoggerFactory.getXLogger(JwtClaimsParsedState.class);

    public JwtClaimsParsedState() {
        super("jwtClaimsParsedState");
    }

    @Override
    public Event stateAction(BcLoginContext ctx) {
        L.entry();
        L.debug("Executing JwtClaimsParsedState action");
        JWTClaimsSet claims = ctx.getJwtClaimsSet();

        Event resultEvent = null;
        try {
            Account exoAccount = ctx.getRegistrar().lookupByChannelId(ctx.getChannelId(), ctx.getChannelUserId());
            if(null != exoAccount) {
                resultEvent = new AccountFoundEvent(ctx, exoAccount);
            } else {
                resultEvent = new AccountNotFoundEvent(ctx, ctx.getChannelId(), ctx.getChannelUserId());
            }
        } catch (Exception e) {
            resultEvent = new InvalidInteractionErrorEvent(ctx, new WarnLoggedResultMeta(RMReg.UNEXPECTED_INTERNAL_ERROR, e));
        }

        // Load the user data from the database and if not found create and send new user event
        return resultEvent;
    }
}
