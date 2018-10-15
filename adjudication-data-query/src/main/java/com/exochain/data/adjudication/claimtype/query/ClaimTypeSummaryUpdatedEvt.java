package com.exochain.data.adjudication.claimtype.query;

import com.exochain.data.adjudication.claimtype.ClaimTypeId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

public class ClaimTypeSummaryUpdatedEvt implements Serializable {
    public static final String JSON_TYPE_ID = "typeId";

    private final ClaimTypeId typeId;

    @JsonCreator
    public ClaimTypeSummaryUpdatedEvt(@JsonProperty(JSON_TYPE_ID) ClaimTypeId typeId) {
        this.typeId = typeId;
    }

    public ClaimTypeId getTypeId() {
        return typeId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("typeId", typeId)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClaimTypeSummaryUpdatedEvt that = (ClaimTypeSummaryUpdatedEvt) o;
        return Objects.equal(typeId, that.typeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(typeId);
    }
}
