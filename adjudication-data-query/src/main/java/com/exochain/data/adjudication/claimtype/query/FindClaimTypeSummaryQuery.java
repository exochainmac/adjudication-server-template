package com.exochain.data.adjudication.claimtype.query;

import com.exochain.data.adjudication.claimtype.ClaimTypeId;

public class FindClaimTypeSummaryQuery {
    private final ClaimTypeId typeId;

    public FindClaimTypeSummaryQuery(ClaimTypeId typeId) {
        this.typeId = typeId;
    }

    public ClaimTypeId getTypeId() {
        return typeId;
    }
}
