package com.exochain.api.bc.fsm.resultbuilding;

import com.exochain.api.bc.RMReg;
import com.exochain.api.bc.fsm.BaseEventHandler;
import com.exochain.api.bc.fsm.bypasserror.BypassErrorEvent;
import com.exochain.result.meta.InfoLoggedResultMeta;
import org.jeasy.states.api.Event;

/**
 * If the result is a success, assigns a new success ResultMeta because the initial
 * value was populated with a stub meta and doesn't have log id etc.
 */
public class ResultBuildingHandler extends BaseEventHandler {
    public static final int CODE_GROUP_SUCCESS = 200;

    @Override
    public void handleEvent(final Event incomingEvent) throws Exception {
        if(incomingEvent instanceof ResultBuildingEvent) {
            ResultBuildingEvent evt = (ResultBuildingEvent) incomingEvent;
            if(evt.getLoginContext().getResultMeta().getCodeGroup() == CODE_GROUP_SUCCESS) {
                // Create a logged successful result because at the start of the state the result meta
                // is just a success stub and doesn't have a log id and isn't logged.
                evt.getLoginContext().setResultMeta(new InfoLoggedResultMeta(RMReg.REQUEST_SUCCEEDED, "BC User ID: [{}]", evt.getLoginContext().getChannelUserId()));
            }
        } else {
            throwInvalidEvent(incomingEvent);
        }
    }
}
