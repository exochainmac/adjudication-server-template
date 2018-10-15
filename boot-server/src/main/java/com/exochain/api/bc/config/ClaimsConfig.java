package com.exochain.api.bc.config;

import com.exochain.data.adjudication.claimtype.query.ClaimTypeSummaryProjection;
import org.axonframework.eventhandling.EventBus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class ClaimsConfig {

    @Bean
    ClaimTypeSummaryProjection claimTypeSummaryProjection(EntityManager entityManager, @Qualifier("queryUpdates") EventBus queryUdateEventBus) {
        return new ClaimTypeSummaryProjection(entityManager, queryUdateEventBus);
    }
}
