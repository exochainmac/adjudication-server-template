package com.exochain.api.bc.fsm.rawreceived;

import com.exochain.api.bc.fsm.BaseEventHandler;
import com.exochain.api.bc.fsm.BcLoginContext;
import org.jeasy.states.api.Event;

/**
 * Takes the raw token fromt he event and puts it in the login context
 */
public class RawTokenReceivedHandler extends BaseEventHandler {
    @Override
    public void handleEvent(final Event incomingEvent) throws Exception {
        if(incomingEvent instanceof RawTokenReceivedEvent) {
            RawTokenReceivedEvent evt = (RawTokenReceivedEvent) incomingEvent;
            BcLoginContext ctx = evt.getLoginContext();
            ctx.setRawInputToken(evt.getRawToken());
            ctx.setApersonaAuthPassed(false);
            ctx.setNewAccount(true);
            ctx.setNewChannelAccount(true);
        } else {
            throwInvalidEvent(incomingEvent);
        }
    }
}
