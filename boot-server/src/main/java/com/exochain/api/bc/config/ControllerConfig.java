package com.exochain.api.bc.config;

import com.exochain.api.bc.fsm.BcFsmFactory;
import com.exochain.api.bc.fsm.FsmFactory;
import com.exochain.jwt.processing.TokenEncrypter;
import com.exochain.jwt.processing.TokenSigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {

    @Autowired
    @Bean
    public FsmFactory fsmFactory(TokenSigner tokenSigner, TokenEncrypter tokenEncrypter, ApiAccountSettings apiAccountSettings) {
        return new BcFsmFactory(tokenSigner, tokenEncrypter, apiAccountSettings);
    }
}
