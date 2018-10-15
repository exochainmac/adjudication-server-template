package com.exochain.api.bc.config.mariadb4j;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * This class just exists to add component scanning if the mariadb4j profile is created
 */
@Profile("mariadb4j")
@Configuration
@ComponentScan("com.exochain.db.mariadb4j")
public class EmbeddedMariaDbConfig {
}
