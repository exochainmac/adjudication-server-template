package com.exochain.api.bc.fsm.rawreceived;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.BcLoginContextProvider;
import org.jeasy.states.api.Event;

/**
 * Sent when BC posts a token to us -- start of flow
 */
public class RawTokenReceivedEvent extends Event implements BcLoginContextProvider {
    private final String rawToken;
    private final BcLoginContext loginContext;

    public RawTokenReceivedEvent(BcLoginContext loginContext, String rawToken) {
        super("rawTokenReceivedEvent");
        this.rawToken = rawToken;
        this.loginContext = loginContext;
    }

    public String getRawToken() {
        return rawToken;
    }

    @Override
    public BcLoginContext getLoginContext() {
        return loginContext;
    }
}
