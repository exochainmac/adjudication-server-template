package com.exochain.api.bc.config;

import com.exochain.axon.command.VersionReturningAggregateAnnotationCommandHandler;
import com.exochain.data.adjudication.claimtype.query.ClaimTypeSummaryProjection;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.axoniq.gdpr.api.FieldEncryptingSerializer;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import org.axonframework.config.AggregateConfigurer;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.config.ModuleConfiguration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.saga.repository.SagaStore;
import org.axonframework.eventhandling.saga.repository.inmemory.InMemorySagaStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AxonConfig {

    @Autowired
    public void configure(EventHandlingConfiguration configuration) {
        configuration.usingTrackingProcessors();
        configuration.registerTrackingProcessor(ClaimTypeSummaryProjection.class.getPackage().getName());
    }

    @Autowired
    public void configure(AxonConfiguration configuration) {

        for(ModuleConfiguration module : configuration.getModules()) {
            if(module instanceof AggregateConfigurer<?>) {
                AggregateConfigurer<?> aggregateConfigurer = (AggregateConfigurer<?>)module;
                // Configure so that version is returned as well
                aggregateConfigurer.configureCommandHandler(c -> new VersionReturningAggregateAnnotationCommandHandler(
                        aggregateConfigurer.aggregateType(), aggregateConfigurer.repository()));
            }
        }
    }

    @Primary
    @Bean
    public Serializer serializer(CryptoEngine cryptoEngine) {
        Serializer internalSerializer = new JacksonSerializer(new ObjectMapper());
        return new FieldEncryptingSerializer(cryptoEngine, internalSerializer);
    }

    @Qualifier("eventSerializer")
    @Bean
    public Serializer eventSerializer(CryptoEngine cryptoEngine) {
        Serializer internalSerializer = new JacksonSerializer(new ObjectMapper());
        return new FieldEncryptingSerializer(cryptoEngine, internalSerializer);
    }

    // Just use in-memory Saga store for now since we aren't using sagas yet
    @Bean
    public SagaStore sagaStore() {
        return new InMemorySagaStore();
    }

    @Bean
    @Qualifier("queryUpdates")
    public EventBus queryUpdateEventBus() {
        return new SimpleEventBus();
    }

}
