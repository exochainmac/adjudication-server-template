package com.exochain.data.adjudication.claimtype.api;

import com.exochain.data.adjudication.claimtype.ClaimTypeId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class ClaimTypeUpdatedEvt implements ClaimTypeInfo {
    public static final String JSON_TYPE_ID = "typeId";
    public static final String JSON_TITLE = "title";
    public static final String JSON_DESCRIPTION = "description";
    public static final String JSON_DATA_VERSION = "dataVersion";
    private final ClaimTypeId typeId;
    private final String title;
    private final String description;
    private final Long dataVersion;

    public ClaimTypeUpdatedEvt(ClaimTypeInfo other) {
        this(other.getTypeId(), other.getTitle(), other.getDescription(), other.getDataVersion());
    }

    @JsonCreator
    public ClaimTypeUpdatedEvt(
            @JsonProperty(JSON_TYPE_ID) ClaimTypeId typeId,
            @JsonProperty(JSON_TITLE) String title,
            @JsonProperty(JSON_DESCRIPTION) String description,
            @JsonProperty(JSON_DATA_VERSION) Long dataVersion) {
        this.typeId = typeId;
        this.title = title;
        this.description = description;
        this.dataVersion = dataVersion;
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

    @Override
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
        ClaimTypeUpdatedEvt that = (ClaimTypeUpdatedEvt) o;
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
