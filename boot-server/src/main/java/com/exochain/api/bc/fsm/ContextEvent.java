package com.exochain.api.bc.fsm;


import com.google.common.base.MoreObjects;
import org.jeasy.states.api.Event;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContextEvent extends Event implements BcLoginContextProvider {
    protected final BcLoginContext loginContext;

    public ContextEvent(String name, BcLoginContext ctx) {
        super(name);
        this.loginContext = checkNotNull(ctx, "Context cannot be null in event constructor");
    }

    @Override
    public BcLoginContext getLoginContext() {
        return loginContext;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("loginContext", loginContext)
                .add("name", name)
                .add("timestamp", timestamp)
                .toString();
    }
}
