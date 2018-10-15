package com.exochain.api.bc.fsm.bypasserror;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ResultEvent;
import com.exochain.result.meta.ResultMetaProvider;

public class BypassErrorEvent extends ResultEvent {
    public BypassErrorEvent(BcLoginContext ctx, ResultMetaProvider resultMetaSource) {
        super(ctx, resultMetaSource);
    }
}
