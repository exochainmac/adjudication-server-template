package com.exochain.data.adjudication.claimtype.query;

import com.exochain.data.adjudication.claimtype.query.ClaimTypeSummary;
import com.google.common.base.MoreObjects;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class FindClaimTypeSummaryResponse {
    private final List<ClaimTypeSummary> data;

    public FindClaimTypeSummaryResponse(List<ClaimTypeSummary> data) {
        checkNotNull(data, "Data list cannot be null");
        this.data = data;
    }

    public List<ClaimTypeSummary> getData() {
        return data;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("data", data)
                .toString();
    }
}
