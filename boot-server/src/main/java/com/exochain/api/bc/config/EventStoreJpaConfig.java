package com.exochain.api.bc.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.jdbc.PersistenceExceptionResolver;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jpa.SQLErrorCodesResolver;
import org.axonframework.monitoring.NoOpMessageMonitor;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.upcasting.event.EventUpcaster;
import org.axonframework.spring.config.AxonConfiguration;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
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
import java.sql.SQLException;

@Configuration
public class EventStoreJpaConfig {

    public static final String PERSISTENCE_UNIT = "events";
    public static final String CONFIGURATION_PROPERTIES_PREFIX = "eventsdb";
    public static final String EVENTS_BEAN_QUALIFIER = "events";
    private static final String DB_SERVICE = "dbServiceBean";

    // Add your JPA packages here
    public static final String[] JPA_PACKAGES = {
            "org.axonframework.eventsourcing.eventstore.jpa"
    };

    /************************************************************************
     * Start with the basic JDBC datasource
     ************************************************************************/

    @Bean
    @Qualifier(EVENTS_BEAN_QUALIFIER)
    @DependsOn(DB_SERVICE) // from the embedded-mariadb module
    @ConfigurationProperties(prefix = CONFIGURATION_PROPERTIES_PREFIX + ".datasource")
    public DataSource eventsDataSource() {
        return DataSourceBuilder.create().build();
    }

    /************************************************************************
     * Using Flyway to do the required schema creation/updates
     ************************************************************************/

    @Bean
    @Qualifier(EVENTS_BEAN_QUALIFIER)
    @ConfigurationProperties(prefix = CONFIGURATION_PROPERTIES_PREFIX + ".flyway")
    public Flyway eventsFlyway(@Qualifier(EVENTS_BEAN_QUALIFIER) DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setBaselineOnMigrate(true);
        return flyway;
    }

    @Bean
    public FlywayMigrationInitializer eventsFlywayMigrationInitializer(@Qualifier(EVENTS_BEAN_QUALIFIER) Flyway flyway) {
        return new FlywayMigrationInitializer(flyway);
    }

    /************************************************************************
     * Configuring JPA
     ************************************************************************/

    @Bean
    @Qualifier(EVENTS_BEAN_QUALIFIER)
    @ConfigurationProperties(prefix = CONFIGURATION_PROPERTIES_PREFIX + ".jpa")
    public JpaProperties eventsJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    @Qualifier(EVENTS_BEAN_QUALIFIER)
    public LocalContainerEntityManagerFactoryBean eventsEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier(EVENTS_BEAN_QUALIFIER) DataSource dataSource,
            @Qualifier(EVENTS_BEAN_QUALIFIER) JpaProperties jpaProperties) {
        return builder
                .dataSource(dataSource)
                .properties(jpaProperties.getProperties())
                .packages(JPA_PACKAGES)
                .persistenceUnit(PERSISTENCE_UNIT)
                .build();
    }

    @Bean
    @Qualifier(EVENTS_BEAN_QUALIFIER)
    public PlatformTransactionManager eventsPlatformTransactionManager(
            @Qualifier(EVENTS_BEAN_QUALIFIER) EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    @Qualifier(EVENTS_BEAN_QUALIFIER)
    public EntityManager eventsSharedEntityManager(@Qualifier(EVENTS_BEAN_QUALIFIER) EntityManagerFactory entityManagerFactory) {
        return SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
    }

    /************************************************************************
     * Axon Framework specific things
     ************************************************************************/

    @Bean
    @Qualifier(EVENTS_BEAN_QUALIFIER)
    public EntityManagerProvider eventsEntityManagerProvider(@Qualifier(EVENTS_BEAN_QUALIFIER) EntityManager entityManager) {
        return new SimpleEntityManagerProvider(entityManager);
    }

    @Bean
    @Qualifier(EVENTS_BEAN_QUALIFIER)
    public TransactionManager eventsTransactionManager(@Qualifier(EVENTS_BEAN_QUALIFIER) PlatformTransactionManager transactionManager) {
        return new SpringTransactionManager(transactionManager);
    }

    @Bean
    @Qualifier(EVENTS_BEAN_QUALIFIER)
    public PersistenceExceptionResolver eventsDataSourcePER(
            @Qualifier(EVENTS_BEAN_QUALIFIER) DataSource dataSource) throws SQLException {
        return new SQLErrorCodesResolver(dataSource);
    }

    @Bean
    public EventStorageEngine eventStorageEngine(Serializer serializer,
                                                 @Qualifier(EVENTS_BEAN_QUALIFIER) PersistenceExceptionResolver persistenceExceptionResolver,
                                                 AxonConfiguration configuration,
                                                 @Qualifier(EVENTS_BEAN_QUALIFIER) EntityManagerProvider entityManagerProvider,
                                                 @Qualifier(EVENTS_BEAN_QUALIFIER) TransactionManager transactionManager) {
        return new JpaEventStorageEngine(serializer, configuration.getComponent(EventUpcaster.class),
                persistenceExceptionResolver, configuration.eventSerializer(), null,
                entityManagerProvider, transactionManager, null, null, true);

    }

    @Bean
    public CommandBus commandBus(@Qualifier(EVENTS_BEAN_QUALIFIER) TransactionManager transactionManager) {
        return new SimpleCommandBus(transactionManager, NoOpMessageMonitor.INSTANCE);
    }

    @Bean
    @Primary
    public EventBus eventBus(EventStorageEngine eventStorageEngine) {
        return new EmbeddedEventStore(eventStorageEngine);
    }

}
