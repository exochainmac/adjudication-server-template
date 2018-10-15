package com.exochain.data.account.api;

import com.exochain.data.account.ExoAccountId;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class CreateExoAccountCmd implements ExoAccountInfo {

    @TargetAggregateIdentifier
    private final ExoAccountId id;
    private final String displayName;
    private final Long dataVersion;

    public CreateExoAccountCmd(ExoAccountId id, String displayName) {
        checkNotNull(id, "ExoAccountId cannot be null");
        checkArgument(StringUtils.isNotBlank(displayName), "Display name cannot be blank");
        this.id = id;
        this.displayName = displayName;
        this.dataVersion = 0L;
    }

    public CreateExoAccountCmd(ExoAccountInfo other) {
        this(other.getId(), other.getDisplayName());
    }

    @Override
    public ExoAccountId getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Long getDataVersion() {
        return dataVersion;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("displayName", displayName)
                .add("dataVersion", dataVersion)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateExoAccountCmd that = (CreateExoAccountCmd) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(displayName, that.displayName) &&
                Objects.equal(dataVersion, that.dataVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, displayName, dataVersion);
    }

}
