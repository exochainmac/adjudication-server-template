package com.exochain.api.bc.fsm;

import com.exochain.result.meta.ResultMeta;
import com.exochain.result.meta.ResultMetaProvider;
import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkNotNull;

public class ResultEvent extends ContextEvent implements ResultMetaProvider {
    protected final ResultMeta resultMeta;

    public ResultEvent(BcLoginContext ctx, ResultMetaProvider resultMetaSource) {
        super("haltingErrorEvent", ctx);
        this.resultMeta = checkNotNull(resultMetaSource.getResultMeta());
    }

    @Override
    public ResultMeta getResultMeta() {
        return resultMeta;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("resultMeta", resultMeta)
                .add("loginContext", loginContext)
                .add("name", name)
                .add("timestamp", timestamp)
                .toString();
    }
}
