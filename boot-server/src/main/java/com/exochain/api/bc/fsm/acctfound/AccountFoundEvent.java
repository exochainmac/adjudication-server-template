package com.exochain.api.bc.fsm.acctfound;

import com.exochain.api.bc.domain.Account;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ContextEvent;
import com.google.common.base.MoreObjects;

public class AccountFoundEvent extends ContextEvent {
    public static final String EVENT_NAME = "accountLoadedEvent";
    private final Account account;

    public AccountFoundEvent(BcLoginContext ctx, Account account) {
        super(EVENT_NAME, ctx);
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
