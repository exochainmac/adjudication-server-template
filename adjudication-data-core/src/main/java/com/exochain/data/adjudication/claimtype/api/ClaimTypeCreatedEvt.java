package com.exochain.data.adjudication.claimtype.api;

import com.exochain.data.adjudication.claimtype.ClaimTypeId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class ClaimTypeCreatedEvt implements ClaimTypeInfo {
    public static final String JSON_TYPE_ID = "typeId";
    public static final String JSON_TITLE = "title";
    public static final String JSON_DESCRIPTION = "description";
    private final ClaimTypeId typeId;
    private final String title;
    private final String description;
    private final Long dataVersion;

    public ClaimTypeCreatedEvt(ClaimTypeInfo other) {
        this(other.getTypeId(), other.getTitle(), other.getDescription());
    }

    @JsonCreator
    public ClaimTypeCreatedEvt(
            @JsonProperty(JSON_TYPE_ID) ClaimTypeId typeId,
            @JsonProperty(JSON_TITLE) String title,
            @JsonProperty(JSON_DESCRIPTION) String description) {
        this.typeId = typeId;
        this.title = title;
        this.description = description;
        // Apparently with Axon the initial version is 0
        this.dataVersion = 0L;
    }

    @Override
    public ClaimTypeId getTypeId() {
        return typeId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public Long getDataVersion() {
        return dataVersion;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("typeId", typeId)
                .add("title", title)
                .add("description", description)
                .add("dataVersion", dataVersion)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClaimTypeCreatedEvt that = (ClaimTypeCreatedEvt) o;
        return Objects.equal(typeId, that.typeId) &&
                Objects.equal(title, that.title) &&
                Objects.equal(description, that.description) &&
                Objects.equal(dataVersion, that.dataVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(typeId, title, description, dataVersion);
    }

}
