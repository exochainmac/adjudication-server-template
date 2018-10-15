package com.exochain.api.bc.fsm.acctfound;

import com.exochain.api.bc.fsm.BaseEventHandler;
import com.exochain.api.bc.fsm.BcLoginContext;
import org.jeasy.states.api.Event;

/**
 * Takes the account data and puts it in the login context
 */
public class AccountFoundHandler extends BaseEventHandler {
    @Override
    public void handleEvent(final Event incomingEvent) throws Exception {
        if(incomingEvent instanceof AccountFoundEvent) {
            AccountFoundEvent evt = (AccountFoundEvent) incomingEvent;
            BcLoginContext ctx = evt.getLoginContext();
            ctx.setAccount(evt.getAccount());
            ctx.setNewAccount(false);
            ctx.setNewChannelAccount(false);
        } else {
            throwInvalidEvent(incomingEvent);
        }
    }
}
