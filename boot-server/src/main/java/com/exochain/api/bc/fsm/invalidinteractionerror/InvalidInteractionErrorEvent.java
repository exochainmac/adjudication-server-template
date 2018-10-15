package com.exochain.api.bc.fsm.invalidinteractionerror;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ResultEvent;
import com.exochain.result.meta.ResultMetaProvider;

public class InvalidInteractionErrorEvent extends ResultEvent {
    public InvalidInteractionErrorEvent(BcLoginContext ctx, ResultMetaProvider resultMetaSource) {
        super(ctx, resultMetaSource);
    }
}
