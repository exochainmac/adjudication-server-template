package com.exochain.api.bc.fsm;

import com.exochain.api.bc.config.ApiAccountSettings;
import com.exochain.api.bc.domain.Account;
import com.exochain.api.bc.service.DatabaseRegistrar;
import com.exochain.api.bc.service.Registrar;
import com.exochain.jwt.processing.TokenProcessor;
import com.exochain.result.meta.ResultMeta;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.jeasy.states.api.FiniteStateMachine;

public interface BcLoginContext {
    String getRawInputToken();

    void setRawInputToken(String rawInputToken);

    FiniteStateMachine getFsm();

    TokenProcessor<SimpleSecurityContext> getJwtTokenProcessor();

    ResultMeta getResultMeta();

    void setResultMeta(ResultMeta resultMeta);

    JWTClaimsSet getJwtClaimsSet();

    void setJwtClaimsSet(JWTClaimsSet jwtClaimsSet);

    Registrar getRegistrar();

    void setAccount(Account account);

    Account getAccount();

    String getChannelId();

    String getChannelUserId();

    void setChannelUserId(String channelUserId);

    String getOutputToken();

    void setOutputToken(String outputToken);

    ApiAccountSettings getApiAccountSettings();

    void setApiAccountSettings(ApiAccountSettings apiAccountSettings);

    Boolean isNewAccount();

    void setNewAccount(Boolean isNew);

    Boolean isNewChannelAccount();

    void setNewChannelAccount(Boolean isNew);

    Boolean isApersonaAuthPassed();

    void setApersonaAuthPassed(Boolean isCompleted);
}
