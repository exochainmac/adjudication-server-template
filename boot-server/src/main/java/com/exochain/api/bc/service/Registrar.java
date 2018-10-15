package com.exochain.api.bc.service;

import com.exochain.api.bc.domain.Account;
import com.exochain.result.exception.ExoStructuredException;

public interface Registrar {
    Account lookupByChannelId(String channelId, String userId) throws ExoStructuredException;
    Account lookupByUserId(String userId) throws ExoStructuredException;
    Account addAccountToChannel(String channelId, String channelUserId, Account account);
    Account create(Account account);
    Account update(Account account);
}
