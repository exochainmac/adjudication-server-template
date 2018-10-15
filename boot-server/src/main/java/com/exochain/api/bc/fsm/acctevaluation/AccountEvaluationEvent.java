package com.exochain.api.bc.fsm.acctevaluation;

import com.exochain.api.bc.domain.Account;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ContextEvent;
import com.google.common.base.MoreObjects;

public class AccountEvaluationEvent extends ContextEvent {
    private final Account account;

    public AccountEvaluationEvent(BcLoginContext ctx, Account account) {
        super(AccountEvaluationEvent.class.getSimpleName(), ctx);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("account", account)
                .add("name", name)
                .add("timestamp", timestamp)
                .toString();
    }
}
