package com.exochain.api.bc.fsm.emailvalidation;

import com.exochain.api.bc.domain.AuthenticationFactor;
import com.exochain.api.bc.fsm.BaseEventHandler;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.emailvalidated.EmailValidatedEvent;
import org.jeasy.states.api.Event;


public class EmailValidationHandler extends BaseEventHandler {
    @Override
    public void handleEvent(final Event incomingEvent) throws Exception {
        if (incomingEvent instanceof EmailValidationEvent) {
            EmailValidationEvent evt = (EmailValidationEvent) incomingEvent;
            BcLoginContext ctx = evt.getLoginContext();
        } else {
            throwInvalidEvent(incomingEvent);
        }
    }
}
