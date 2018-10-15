package com.exochain.api.bc.fsm.acctnotfound;

import com.exochain.api.bc.RMReg;
import com.exochain.api.bc.domain.Account;
import com.exochain.api.bc.domain.AuthenticationFactor;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ClaimExtractor;
import com.exochain.api.bc.fsm.ExecutionState;
import com.exochain.api.bc.fsm.acctinitiation.AccountInitiationEvent;
import com.exochain.api.bc.fsm.bypasserror.BypassErrorEvent;
import com.exochain.api.bc.fsm.chnlacctinitiation.ChannelAccountInitiationEvent;
import com.exochain.api.bc.fsm.exception.PassthroughException;
import com.exochain.api.bc.service.Registrar;
import com.exochain.result.meta.WarnLoggedResultMeta;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import org.jeasy.states.api.Event;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * The account was not found in the channel.  Create a new channel account for them or an
 * entirely new exoAccount if we haven't seen them before.
 */
public class AccountNotFoundState extends ExecutionState {
    public static final String STATE_NAME = "accountNotFoundState";
    private static final XLogger L = XLoggerFactory.getXLogger(AccountNotFoundState.class);
    private static final TimeBasedGenerator UUID_GENERATOR = Generators.timeBasedGenerator();

    public AccountNotFoundState() {
        super(STATE_NAME);
    }

    @Override
    public Event stateAction(BcLoginContext ctx) {
        L.entry();

        Event resultEvent = null;

        try {

            // First find out if we have an exoAccount with the same userId (email)
            JWTClaimsSet claimsSet = ctx.getJwtClaimsSet();
            String emailAddress = ClaimExtractor.extractSingleSubjectEmail(claimsSet).toLowerCase();

            Registrar registrar = ctx.getRegistrar();
            Account account = registrar.lookupByUserId(emailAddress);

            if(null == account) {
                // We don't have them at all so create their account
                String cellPhone = ClaimExtractor.extractSingleSubjectCell(claimsSet);
                account = buildAccountData(emailAddress, cellPhone);
                // account = registrar.create(account);
                resultEvent = new AccountInitiationEvent(ctx, account);
            } else {
                // registrar.addAccountToChannel(ctx.getChannelId(), ctx.getChannelUserId(), account);
                resultEvent = new ChannelAccountInitiationEvent(ctx, account);
            }
        } catch(PassthroughException e) {
            resultEvent = new BypassErrorEvent(ctx, e.getResultMeta());
        } catch (Exception e) {
            resultEvent = new BypassErrorEvent(ctx, new WarnLoggedResultMeta(RMReg.UNEXPECTED_INTERNAL_ERROR, e));
        }

        // punt for now
        return resultEvent;
    }

    private Account buildAccountData(String emailAddress, String cellPhone) {
        Account account = new Account();
        account.setId(UUID_GENERATOR.generate().toString());

        account.setUserId(emailAddress.toLowerCase());

        AuthenticationFactor emailFactor = new AuthenticationFactor(AuthenticationFactor.AuthType.EMAIL.name(),
                emailAddress, false);
        account.setEmailFactor(emailFactor);

        AuthenticationFactor cellFactor = new AuthenticationFactor(AuthenticationFactor.AuthType.SMS.name(),
                cellPhone, false);
        account.setCellFactor(cellFactor);
        return account;
    }

}
