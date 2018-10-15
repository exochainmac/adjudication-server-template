package com.exochain.api.bc.fsm.cellvalidated;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ContextEvent;
import com.google.common.base.MoreObjects;

public class CellValidatedEvent extends ContextEvent {
    public CellValidatedEvent(BcLoginContext ctx) {
        super("cellValidatedEvent", ctx);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("timestamp", timestamp)
                .toString();
    }
}
