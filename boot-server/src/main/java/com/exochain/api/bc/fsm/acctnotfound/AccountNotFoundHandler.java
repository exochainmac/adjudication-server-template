package com.exochain.api.bc.fsm.acctnotfound;

import com.exochain.api.bc.fsm.BaseEventHandler;
import org.jeasy.states.api.Event;

/**
 * Takes the account data and puts it in the login context
 */
public class AccountNotFoundHandler extends BaseEventHandler {
    @Override
    public void handleEvent(final Event incomingEvent) throws Exception {
        if(incomingEvent instanceof AccountNotFoundEvent) {
            AccountNotFoundEvent evt = (AccountNotFoundEvent) incomingEvent;
            evt.getLoginContext().setChannelUserId(evt.getChannelUserId());
        } else {
            throwInvalidEvent(incomingEvent);
        }
    }
}
