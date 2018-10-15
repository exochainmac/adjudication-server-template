package com.exochain.api.bc.fsm.cellvalidated;

import com.exochain.api.bc.domain.AuthenticationFactor;
import com.exochain.api.bc.fsm.BaseEventHandler;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.emailvalidated.EmailValidatedEvent;
import org.jeasy.states.api.Event;

/**
 * Takes the raw token fromt he event and puts it in the login context
 */
public class CellValidatedHandler extends BaseEventHandler {
    @Override
    public void handleEvent(final Event incomingEvent) throws Exception {
        if(incomingEvent instanceof CellValidatedEvent) {
            CellValidatedEvent evt = (CellValidatedEvent) incomingEvent;
            BcLoginContext ctx = evt.getLoginContext();
            AuthenticationFactor oldFactor = ctx.getAccount().getCellFactor();
            AuthenticationFactor newFactor = new AuthenticationFactor(oldFactor.getType(), oldFactor.getOtpAddress(), true);
            ctx.getAccount().setCellFactor(newFactor);
        } else {
            throwInvalidEvent(incomingEvent);
        }
    }
}
