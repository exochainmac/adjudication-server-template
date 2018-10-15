package com.exochain.api.bc.fsm;

import com.exochain.api.bc.RMReg;
import com.exochain.result.meta.WarnLoggedResultMeta;
import org.jeasy.states.api.Event;
import org.jeasy.states.api.EventHandler;

/**
 * Takes the raw token fromt he event and puts it in the login context
 */
public abstract class BaseEventHandler implements EventHandler {

    @Override
    abstract public void handleEvent(final Event incomingEvent) throws Exception;

    protected void throwInvalidEvent(Event event) throws InvalidEventTypeException {
        throw new InvalidEventTypeException(new WarnLoggedResultMeta(RMReg.UNEXPECTED_INTERNAL_ERROR,
                "Invalid event type {} with name {} received in {} event handler: {}",
                event.getClass().getName(),
                event.getName(),
                getClass().getName()));
    }
}
