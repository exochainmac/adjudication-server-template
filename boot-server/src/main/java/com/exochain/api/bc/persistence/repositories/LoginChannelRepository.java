package com.exochain.api.bc.persistence.repositories;

import com.exochain.api.bc.persistence.entities.LoginChannel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface LoginChannelRepository extends CrudRepository<LoginChannel, String> {
    Collection<LoginChannel> findAll();
    Collection<LoginChannel> findAllByTitle(String title);
}
