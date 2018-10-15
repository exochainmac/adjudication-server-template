package com.exochain.api.bc.fsm.resultbuilding;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ContextEvent;

public class ResultBuildingEvent extends ContextEvent {
    public static final String EVENT_NAME = "resultBuildingEvent";
    public ResultBuildingEvent(BcLoginContext ctx) {
        super(EVENT_NAME, ctx);
    }
}
