package com.exochain.data.adjudication.claimtype.api;

import com.exochain.data.adjudication.claimtype.ClaimTypeId;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class CreateClaimTypeCmd implements ClaimTypeInfo {

    @TargetAggregateIdentifier
    private final ClaimTypeId typeId;
    private final String title;
    private final String description;
    private final Long dataVersion;

    public CreateClaimTypeCmd(ClaimTypeInfo other) {
        this(other.getTypeId(), other.getTitle(), other.getDescription());
    }

    public CreateClaimTypeCmd(ClaimTypeId typeId, String title, String description) {
        this.typeId = typeId;
        this.title = title;
        this.description = description;
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
        CreateClaimTypeCmd that = (CreateClaimTypeCmd) o;
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
