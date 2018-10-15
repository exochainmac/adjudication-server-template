package com.exochain.api.bc.fsm.resultready;

import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ContextEvent;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;

public class ResultReadyEvent extends ContextEvent {
    public static final String EVENT_NAME = "resultReadyEvent";

    private final String serializedToken;

    public ResultReadyEvent(BcLoginContext ctx, String serializedToken) {
        super(EVENT_NAME, ctx);
        checkArgument(StringUtils.isNotBlank(serializedToken), "Serialized token cannot be blank");
        this.serializedToken = serializedToken;
    }

    public String getSerializedToken() {
        return serializedToken;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("serializedToken", serializedToken)
                .add("name", name)
                .add("timestamp", timestamp)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultReadyEvent that = (ResultReadyEvent) o;
        return Objects.equal(serializedToken, that.serializedToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serializedToken);
    }
}
