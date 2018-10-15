package com.exochain.api.bc.fsm.acctinitiation;

import com.exochain.api.bc.fsm.BaseEventHandler;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.acctfound.AccountFoundEvent;
import org.jeasy.states.api.Event;

public class AccountInitiationHandler extends BaseEventHandler {
    @Override
    public void handleEvent(final Event incomingEvent) throws Exception {
        if(incomingEvent instanceof AccountFoundEvent) {
            AccountFoundEvent evt = (AccountFoundEvent) incomingEvent;
            BcLoginContext ctx = evt.getLoginContext();
            ctx.setAccount(evt.getAccount());
            ctx.setNewAccount(true);
            // If it is a new account (above) then it must be a new channel account as well
            ctx.setNewChannelAccount(true);
        } else {
            throwInvalidEvent(incomingEvent);
        }
    }
}
