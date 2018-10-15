package com.exochain.data.account.api;

import com.exochain.data.account.ExoAccountId;

public interface ExoAccountInfo {
    ExoAccountId getId();
    Long getDataVersion();
    String getDisplayName();
}
