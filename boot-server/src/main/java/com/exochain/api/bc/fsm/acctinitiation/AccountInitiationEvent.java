package com.exochain.api.bc.fsm.acctinitiation;

import com.exochain.api.bc.domain.Account;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ContextEvent;
import com.google.common.base.MoreObjects;

public class AccountInitiationEvent extends ContextEvent {
    private final Account account;

    public AccountInitiationEvent(BcLoginContext ctx, Account account) {
        super(AccountInitiationEvent.class.getSimpleName(), ctx);
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
