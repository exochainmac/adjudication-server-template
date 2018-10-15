package com.exochain.api.bc.persistence.repositories;

import com.exochain.api.bc.persistence.entities.LoginChannelAccount;
import com.exochain.api.bc.persistence.entities.LoginChannelAccountId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginChannelAccountRepository extends CrudRepository<LoginChannelAccount, LoginChannelAccountId> {

    LoginChannelAccount findByChannelIdAndChannelUserId(String channelId, String channelUserId);
}
