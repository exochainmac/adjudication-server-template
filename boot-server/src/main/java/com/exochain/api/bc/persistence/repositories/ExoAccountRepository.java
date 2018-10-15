package com.exochain.api.bc.persistence.repositories;

import com.exochain.api.bc.persistence.entities.ExoAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExoAccountRepository extends CrudRepository<ExoAccount, String> {
    ExoAccount findByUserIdHash(String userIdHash);
}
