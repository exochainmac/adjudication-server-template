package com.exochain.api.bc.fsm;

import com.exochain.result.meta.ResultMetaProvider;

import static com.google.common.base.Preconditions.checkNotNull;

public class ErrorEvent extends ResultEvent {
    public ErrorEvent(BcLoginContext ctx, ResultMetaProvider resultMetaSource) {
        super(ctx, resultMetaSource);
    }
}
