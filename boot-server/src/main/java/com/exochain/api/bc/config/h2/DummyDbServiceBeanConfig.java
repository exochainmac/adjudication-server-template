package com.exochain.api.bc.config.h2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("h2db")
@Configuration
public class DummyDbServiceBeanConfig {
    private static final String DB_SERVICE = "dbServiceBean";

    // Needed to satisfy a bean name dependency normally provided by embedded mariadb4j
    @Bean(name = DB_SERVICE)
    public DummyBean registerDummyBean() {
        return new DummyBean();
    }

    static class DummyBean {

    }
}
