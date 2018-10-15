package com.exochain.data.adjudication.claimtype.api;

import com.exochain.data.adjudication.claimtype.ClaimTypeId;

public interface ClaimTypeInfo {
    ClaimTypeId getTypeId();

    String getTitle();

    String getDescription();

    Long getDataVersion();
}
