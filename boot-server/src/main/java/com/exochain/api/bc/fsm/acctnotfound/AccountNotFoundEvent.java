package com.exochain.api.bc.fsm.acctnotfound;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ContextEvent;
import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkNotNull;

public class AccountNotFoundEvent extends ContextEvent {
    public static final String EVENT_NAME = "accountNotFoundEvent";
    private final String channelUserId;
    private final String channelId;

    public AccountNotFoundEvent(BcLoginContext ctx, String channelId, String channelUserId) {
        super(EVENT_NAME, ctx);
        this.channelUserId = checkNotNull(channelUserId);
        this.channelId = checkNotNull(channelId);
    }

    public String getChannelUserId() {
        return channelUserId;
    }

    public String getChannelId() {
        return channelId;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channelUserId", channelUserId)
                .add("channelId", channelId)
                .add("name", name)
                .add("timestamp", timestamp)
                .toString();
    }
}
