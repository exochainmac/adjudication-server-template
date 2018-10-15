package com.exochain.api.bc.config;

import com.exochain.ap.auth.ApAuthenticationManager;
import com.exochain.ap.auth.StandardApAuthenticationManager;
import com.exochain.ap.core.ApApiConfigurationInfo;
import com.exochain.ap.core.ApApiConfigurationProperties;
import com.exochain.ap.core.StandardApApiConfiguration;
import com.exochain.ap.core.StandardRawApApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;

@Configuration
public class APersonaConfiguration {

    @ConfigurationProperties(prefix = "apersona")
    @Bean
    ApApiConfigurationProperties apersonaApiConfigurationProperties() {
        return new ApApiConfigurationProperties();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    ApApiConfigurationInfo apersonaApiConfiguration() throws URISyntaxException {
        return new StandardApApiConfiguration(restTemplate(), apersonaApiConfigurationProperties());
    }

    @Bean
    StandardRawApApi apApi() throws URISyntaxException {
        return new StandardRawApApi(apersonaApiConfiguration());
    }

    @Bean
    ApAuthenticationManager apAuthenticationManager() throws URISyntaxException {
        return new StandardApAuthenticationManager(apApi(), apersonaApiConfiguration().getExoWebHost());
    }
}
