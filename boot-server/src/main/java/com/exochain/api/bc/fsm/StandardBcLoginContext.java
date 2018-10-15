package com.exochain.api.bc.fsm;

import com.exochain.ap.auth.ApAuthenticationManager;
import com.exochain.api.bc.config.ApiAccountSettings;
import com.exochain.api.bc.domain.Account;
import com.exochain.api.bc.service.Registrar;
import com.exochain.jwt.processing.TokenProcessor;
import com.exochain.result.meta.ResultMeta;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import org.jeasy.states.api.FiniteStateMachine;

public class StandardBcLoginContext implements BcLoginContext {
    private static final String BC_CHANNEL_ID = "94f6f147-38f3-11e8-b12b-b3a10111c182";

    private String rawInputToken;
    private ResultMeta resultMeta;
    private final FiniteStateMachine fsm;
    private final TokenProcessor<SimpleSecurityContext> jwtTokenProcessor;
    private ApiAccountSettings apiAccountSettings;
    private JWTClaimsSet jwtClaimsSet;
    private final Registrar registrar;
    private Account account;
    private String channelUserId;
    private String outputToken;
    private final ApAuthenticationManager apAuthenticationManager;
    private Boolean newAccount = true;
    private Boolean newChannelAccount = true;
    private Boolean aPersonalAuthPassed = false;

    public StandardBcLoginContext(FiniteStateMachine fsm, TokenProcessor<SimpleSecurityContext> jwtTokenProcessor,
                                  Registrar registrar, ApAuthenticationManager apAuthenticationManager) {
        this.fsm = fsm;
        this.jwtTokenProcessor = jwtTokenProcessor;
        this.registrar = registrar;
        this.apAuthenticationManager = apAuthenticationManager;
    }

    @Override
    public String getRawInputToken() {
        return rawInputToken;
    }

    @Override
    public void setRawInputToken(String rawInputToken) {
        this.rawInputToken = rawInputToken;
    }

    @Override
    public FiniteStateMachine getFsm() {
        return fsm;
    }

    @Override
    public TokenProcessor<SimpleSecurityContext> getJwtTokenProcessor() {
        return jwtTokenProcessor;
    }

    @Override
    public ResultMeta getResultMeta() {
        return resultMeta;
    }

    @Override
    public void setResultMeta(ResultMeta resultMeta) {
        this.resultMeta = resultMeta;
    }

    @Override
    public Registrar getRegistrar() {
        return registrar;
    }

    @Override
    public JWTClaimsSet getJwtClaimsSet() {
        return jwtClaimsSet;
    }

    @Override
    public void setJwtClaimsSet(JWTClaimsSet jwtClaimsSet) {
        this.jwtClaimsSet = jwtClaimsSet;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String getChannelId() {
        return BC_CHANNEL_ID;
    }

    @Override
    public String getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(String channelUserId) {
        this.channelUserId = channelUserId;
    }

    @Override
    public String getOutputToken() {
        return outputToken;
    }

    @Override
    public void setOutputToken(String outputToken) {
        this.outputToken = outputToken;
    }

    @Override
    public ApiAccountSettings getApiAccountSettings() {
        return apiAccountSettings;
    }

    @Override
    public void setApiAccountSettings(ApiAccountSettings apiAccountSettings) {
        this.apiAccountSettings = apiAccountSettings;
    }

    @Override
    public Boolean isNewAccount() {
        return newAccount;
    }

    @Override
    public void setNewAccount(Boolean newAccount) {
        this.newAccount = newAccount;
    }

    @Override
    public Boolean isNewChannelAccount() {
        return newChannelAccount;
    }

    @Override
    public void setNewChannelAccount(Boolean newChannelAccount) {
        this.newChannelAccount = newChannelAccount;
    }

    @Override
    public Boolean isApersonaAuthPassed() {
        return aPersonalAuthPassed;
    }

    @Override
    public void setApersonaAuthPassed(Boolean liveAuthCompleted) {
        this.aPersonalAuthPassed = liveAuthCompleted;
    }

    public ApAuthenticationManager getApAuthenticationManager() {
        return apAuthenticationManager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandardBcLoginContext that = (StandardBcLoginContext) o;
        return Objects.equal(rawInputToken, that.rawInputToken) &&
                Objects.equal(resultMeta, that.resultMeta) &&
                Objects.equal(fsm, that.fsm) &&
                Objects.equal(jwtTokenProcessor, that.jwtTokenProcessor) &&
                Objects.equal(apiAccountSettings, that.apiAccountSettings) &&
                Objects.equal(jwtClaimsSet, that.jwtClaimsSet) &&
                Objects.equal(registrar, that.registrar) &&
                Objects.equal(account, that.account) &&
                Objects.equal(channelUserId, that.channelUserId) &&
                Objects.equal(outputToken, that.outputToken) &&
                Objects.equal(apAuthenticationManager, that.apAuthenticationManager) &&
                Objects.equal(newAccount, that.newAccount) &&
                Objects.equal(newChannelAccount, that.newChannelAccount) &&
                Objects.equal(aPersonalAuthPassed, that.aPersonalAuthPassed);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rawInputToken, resultMeta, fsm, jwtTokenProcessor, apiAccountSettings, jwtClaimsSet, registrar, account, channelUserId, outputToken, apAuthenticationManager, newAccount, newChannelAccount, aPersonalAuthPassed);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("resultMeta", resultMeta)
                .add("account", account)
                .add("channelUserId", channelUserId)
                .add("newAccount", newAccount)
                .add("newChannelAccount", newChannelAccount)
                .add("aPersonalAuthPassed", aPersonalAuthPassed)
                .toString();
    }
}
