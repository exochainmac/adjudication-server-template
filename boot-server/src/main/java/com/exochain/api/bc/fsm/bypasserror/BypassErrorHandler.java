package com.exochain.api.bc.fsm.bypasserror;

import com.exochain.api.bc.fsm.BaseEventHandler;
import com.exochain.api.bc.fsm.rawreceived.RawTokenReceivedEvent;
import org.jeasy.states.api.Event;

/**
 * Takes the raw token fromt he event and puts it in the login context
 */
public class BypassErrorHandler extends BaseEventHandler {
    @Override
    public void handleEvent(final Event incomingEvent) throws Exception {
        if(incomingEvent instanceof BypassErrorEvent) {
            BypassErrorEvent evt = (BypassErrorEvent) incomingEvent;
            evt.getLoginContext().setResultMeta(evt.getResultMeta());
        } else {
            throwInvalidEvent(incomingEvent);
        }
    }
}
