package com.exochain.api.bc.fsm.catastrophicerror;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ResultEvent;
import com.exochain.result.meta.ResultMetaProvider;

public class CatastrophicErrorEvent extends ResultEvent {
    public CatastrophicErrorEvent(BcLoginContext ctx, ResultMetaProvider resultMetaSource) {
        super(ctx, resultMetaSource);
    }
}
