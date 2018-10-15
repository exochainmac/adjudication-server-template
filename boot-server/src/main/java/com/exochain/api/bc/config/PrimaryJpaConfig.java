package com.exochain.api.bc.config;

import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import io.axoniq.gdpr.cryptoengine.jpa.JpaCryptoEngine;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class PrimaryJpaConfig {
    private static final Logger L = LoggerFactory.getLogger(PrimaryJpaConfig.class);
    private static final String DB_SERVICE = "dbServiceBean";

    // Add your JPA packages here
    public static final String[] JPA_PACKAGES = {
            "org.axonframework.eventhandling.tokenstore.jpa",
            "org.axonframework.eventhandling.saga.repository.jpa",
            "io.axoniq.gdpr.cryptoengine.jpa",
            "com.exochain.api.bc.persistence",
            "com.exochain.data.adjudication.claimtype.query"
    };

    public static final String PERSISTENCE_UNIT = "primary";
    public static final String CONFIGURATION_PROPERTIES_PREFIX = "primarydb";

    /************************************************************************
     * Using Flyway to do the required schema creation/updates
     ************************************************************************/

    @Bean
    @Primary
    @ConfigurationProperties(prefix = CONFIGURATION_PROPERTIES_PREFIX + ".flyway")
    @DependsOn(DB_SERVICE ) // from the embedded-mariadb module
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        return flyway;
    }

    @Bean
    public FlywayMigrationInitializer flywayMigrationInitializer(Flyway flyway) {
        return new FlywayMigrationInitializer(flyway);
    }

    /************************************************************************
     * Configuring JPA
     ************************************************************************/

    @Bean
    @Primary
    @ConfigurationProperties(prefix = CONFIGURATION_PROPERTIES_PREFIX + ".jpa")
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            DataSource dataSource,
            JpaProperties jpaProperties) {


        return builder
                .dataSource(dataSource)
                .properties(jpaProperties.getProperties())
                .packages(JPA_PACKAGES)
                .persistenceUnit(PERSISTENCE_UNIT)
                .build();
    }

    @Bean
    @Qualifier("transactionManager")
    @Primary
    public PlatformTransactionManager platformTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    @Primary
    public EntityManager sharedEntityManager(EntityManagerFactory entityManagerFactory) {
        return SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
    }

    /************************************************************************
     * Axon Framework specific things
     ************************************************************************/

    @Bean
    @Primary
    public EntityManagerProvider primaryEntityManagerProvider(EntityManager entityManager) {
        return new SimpleEntityManagerProvider(entityManager);
    }

    @Bean
    @Primary
    public TransactionManager primaryTransactionManager(PlatformTransactionManager platformTransactionManager) {
        return new SpringTransactionManager(platformTransactionManager);
    }

    @Bean
    public TokenStore tokenStore(Serializer serializer, EntityManagerProvider entityManagerProvider) {
        return new JpaTokenStore(entityManagerProvider, serializer);
    }

    @Bean
    public CryptoEngine cryptoEngine(EntityManagerFactory emf) {
        return new JpaCryptoEngine(emf);
    }

}
