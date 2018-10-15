package com.exochain.data.account.api;

import com.exochain.data.account.ExoAccountId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class ExoAccountCreatedEvt implements ExoAccountInfo {
    public static final String JSON_ID = "id";
    public static final String JSON_DISPLAY_NAME = "displayName";
    private final ExoAccountId id;
    private final String displayName;
    private final Long dataVersion;

    @JsonCreator
    public ExoAccountCreatedEvt(
            @JsonProperty(JSON_ID) ExoAccountId id,
            @JsonProperty(JSON_DISPLAY_NAME) String displayName) {
        this.id = id;
        this.displayName = displayName;
        this.dataVersion = 0L;
    }

    public ExoAccountCreatedEvt(ExoAccountInfo other) {
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
        ExoAccountCreatedEvt that = (ExoAccountCreatedEvt) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(displayName, that.displayName) &&
                Objects.equal(dataVersion, that.dataVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, displayName, dataVersion);
    }

}
