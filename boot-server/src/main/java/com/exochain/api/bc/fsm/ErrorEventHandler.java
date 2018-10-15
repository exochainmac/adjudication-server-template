package com.exochain.api.bc.fsm;

import org.jeasy.states.api.Event;

/**
 * Takes the result metadata from the event and puts it in the context
 */
public class ErrorEventHandler extends BaseEventHandler {
    @Override
    public void handleEvent(final Event incomingEvent) throws Exception {
        if(incomingEvent instanceof ResultEvent) {
            ResultEvent evt = (ResultEvent) incomingEvent;
            evt.getLoginContext().setResultMeta(evt.getResultMeta());
        } else {
            throwInvalidEvent(incomingEvent);
        }
    }
}
