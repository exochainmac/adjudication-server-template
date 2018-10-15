package com.exochain.api.bc.fsm.resultready;

import com.exochain.api.bc.fsm.BaseEventHandler;
import com.exochain.api.bc.fsm.rawreceived.RawTokenReceivedEvent;
import org.jeasy.states.api.Event;

/**
 * Takes the serialized token from the event and puts it in the login context
 */
public class ResultReadyHandler extends BaseEventHandler {
    @Override
    public void handleEvent(final Event incomingEvent) throws Exception {
        if(incomingEvent instanceof ResultReadyEvent) {
            ResultReadyEvent evt = (ResultReadyEvent) incomingEvent;
            evt.getLoginContext().setOutputToken(evt.getSerializedToken());
        } else {
            throwInvalidEvent(incomingEvent);
        }
    }
}
