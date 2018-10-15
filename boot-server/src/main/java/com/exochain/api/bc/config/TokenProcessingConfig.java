package com.exochain.api.bc.config;

import com.exochain.jwt.claims.ClaimNames;
import com.exochain.jwt.claims.CompositeClaimsVerifier;
import com.exochain.jwt.claims.ExpiredClaimVerifier;
import com.exochain.jwt.claims.NotBeforeClaimVerifier;
import com.exochain.jwt.claims.StringEqualClaimVerifier;
import com.exochain.jwt.claims.StringNotEmptyClaimVerifier;
import com.exochain.jwt.claims.StringOrStringListContainsClaimVerifier;
import com.exochain.jwt.keys.ExoKeySourceException;
import com.exochain.jwt.keys.InternalKeySource;
import com.exochain.jwt.keys.JWKSourceFactory;
import com.exochain.jwt.keys.PartnerKeySource;
import com.exochain.jwt.keys.StandardJWKSourceFactory;
import com.exochain.jwt.processing.StandardTokenEncrypter;
import com.exochain.jwt.processing.StandardTokenProcessor;
import com.exochain.jwt.processing.StandardTokenProcessorConfiguration;
import com.exochain.jwt.processing.StandardTokenSigner;
import com.exochain.jwt.processing.TokenEncrypter;
import com.exochain.jwt.processing.TokenProcessor;
import com.exochain.jwt.processing.TokenProcessorConfiguration;
import com.exochain.jwt.processing.TokenSigner;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.proc.JWTClaimsSetVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration 
public class TokenProcessingConfig {
    private  static final Logger L = LoggerFactory.getLogger(TokenProcessingConfig.class);

    @Bean
    TokenProcessor<SimpleSecurityContext> tokenProcessor(TokenProcessorConfiguration<SimpleSecurityContext> configuration) throws ExoKeySourceException {
        return new StandardTokenProcessor<>(configuration);
    }

    @Bean
    TokenProcessorConfiguration<SimpleSecurityContext> processorConfiguration(JWTClaimsSetVerifier<SimpleSecurityContext> claimsVerifier,
                                                                              InternalKeySource<SimpleSecurityContext> exoKeySource,
                                                                              PartnerKeySource<SimpleSecurityContext> partnerKeySource) throws ExoKeySourceException {
        return StandardTokenProcessorConfiguration.B.<SimpleSecurityContext>aBcTokenProcessorConfiguration()
                .withClaimsVerifier(claimsVerifier)
                .withInternalKeySource(exoKeySource)
                .withPartnerKeySource(partnerKeySource)
                .build();
    }

    @Bean
    JWTClaimsSetVerifier<SimpleSecurityContext> claimsVerifier(ApiAccountSettings apiAccountSettings) {
        ArrayList<JWTClaimsSetVerifier<SimpleSecurityContext>> verifiers = new ArrayList<>(10);

        // Required the token to be active
        verifiers.add(new ExpiredClaimVerifier());

        // Required it is not used too early
        verifiers.add(new NotBeforeClaimVerifier());

        // Require the JWT id is set in the token
        verifiers.add(new StringNotEmptyClaimVerifier(ClaimNames.STD_JWT_ID.getClaimName()));

        // Require the correct audience claim
        verifiers.add(new StringOrStringListContainsClaimVerifier(ClaimNames.STD_AUDIENCE.getClaimName(), apiAccountSettings.getExoJwtAudience()));

        // Require the correct issuer
        verifiers.add(new StringEqualClaimVerifier(ClaimNames.STD_ISSUER.getClaimName(), apiAccountSettings.getClientIssuer()));

        return new CompositeClaimsVerifier(verifiers);
    }

    @Bean
    InternalKeySource<SimpleSecurityContext> exoKeySource(JWKSourceFactory<SimpleSecurityContext> jwkSourceFactory,
                                                          ApiAccountSettings apiAccountSettings) throws ExoKeySourceException {
        return jwkSourceFactory.getInternalById(apiAccountSettings.getPrivateKeysLocation(), null);
    }

    @Bean
    PartnerKeySource<SimpleSecurityContext> partnerPublicKeySource(JWKSourceFactory<SimpleSecurityContext> jwkSourceFactory,
                                                                   ApiAccountSettings accountSettings) throws ExoKeySourceException {
        return jwkSourceFactory.getPartnerById(accountSettings.getPublicKeysLocation(), null);
    }

    @Bean
    JWKSourceFactory<SimpleSecurityContext> jwkSourceFactory() {
        return new StandardJWKSourceFactory<>();
    }

    @Bean
    TokenSigner tokenSigner(InternalKeySource<SimpleSecurityContext> exoKeySource) {
        return new StandardTokenSigner(exoKeySource);
    }

    @Bean
    TokenEncrypter tokenEncrypter(PartnerKeySource<SimpleSecurityContext> partnerPublicKeySource) {
        return new StandardTokenEncrypter(partnerPublicKeySource);
    }
}