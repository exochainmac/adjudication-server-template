package com.exochain.api.bc.fsm.acctevaluation;

import com.exochain.api.bc.fsm.BaseEventHandler;
import com.exochain.api.bc.fsm.acctfound.AccountFoundEvent;
import org.jeasy.states.api.Event;

/**
 * Takes the account data and puts it in the login context
 */
public class AccountEvaluationHandler extends BaseEventHandler {
    @Override
    public void handleEvent(final Event incomingEvent) throws Exception {
        if(incomingEvent instanceof AccountFoundEvent) {
            AccountFoundEvent evt = (AccountFoundEvent) incomingEvent;
            evt.getLoginContext().setAccount(evt.getAccount());
        } else {
            throwInvalidEvent(incomingEvent);
        }
    }
}
