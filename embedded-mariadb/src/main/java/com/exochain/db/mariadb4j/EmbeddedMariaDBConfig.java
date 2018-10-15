package com.exochain.db.mariadb4j;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Only used for local dev testing when the spring "mariadb4j" profile is active.
 * Also the Maven "mariadb4j" profile must be active as well.
 */
@Configuration
@Profile("mariadb4j")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class EmbeddedMariaDBConfig {
    private static final Logger L = LoggerFactory.getLogger(EmbeddedMariaDBConfig.class);
    private static final String DB_SERVICE = "dbServiceBean";

    @Bean(name = {DB_SERVICE})
    MariaDB4jSpringService mariaDB4jSpringService() {
        L.info("Initializing MariaDB4j service");
        return new MariaDB4jSpringService();
    }

    @Bean
    @Primary
    @DependsOn(DB_SERVICE)
    DataSource dataSource(MariaDB4jSpringService mdb, DataSourceProperties dataSourceProperties) throws ManagedProcessException {
        String dbName = dataSourceProperties.getName();
        L.debug("Embedded MariaDB datasource properties from spring: [{}]", dataSourceProperties);
        mdb.getDB().createDB(dbName);
        mdb.getDB().createDB(dbName + "evt");

        if(L.isDebugEnabled()) {
            DBConfigurationBuilder ecfg = mdb.getConfiguration();
            L.debug("JDBC URL for embedded MariaDB as reported by driver: [{}]", ecfg.getURL(dbName));
            L.debug("JDBC URL from spring config: [{}]", dataSourceProperties.getUrl());
            L.debug("JDBC Username: [{}]", dataSourceProperties.getUsername());
            L.debug("JDBC Password: [{}]", dataSourceProperties.getPassword());
        }

        return DataSourceBuilder
                .create()
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .url(dataSourceProperties.getUrl())
                .driverClassName(dataSourceProperties.getDriverClassName())
                .build();
    }

}
