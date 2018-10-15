package com.exochain.api.bc.fsm.acctevaluation;

import com.exochain.api.bc.RMReg;
import com.exochain.api.bc.domain.Account;
import com.exochain.api.bc.domain.AuthenticationFactor;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.ExecutionState;
import com.exochain.api.bc.fsm.acctvalidated.AccountValidatedEvent;
import com.exochain.api.bc.fsm.invalidinteractionerror.InvalidInteractionErrorEvent;
import com.exochain.api.bc.fsm.cellvalidation.CellValidationEvent;
import com.exochain.api.bc.fsm.emailvalidation.EmailValidationEvent;
import com.exochain.result.meta.WarnLoggedResultMeta;
import org.jeasy.states.api.Event;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class AccountEvaluationState extends ExecutionState {
    private static final XLogger L = XLoggerFactory.getXLogger(AccountEvaluationState.class);


    public AccountEvaluationState() {
        super(AccountEvaluationState.class.getSimpleName());
    }

    @Override
    public Event stateAction(BcLoginContext ctx) {
        L.entry();

        Event resultEvent = null;

        try {
            Account account = ctx.getAccount();
            AuthenticationFactor emailFactor = account.getEmailFactor();

            if (emailFactor.isValidated()) {
                AuthenticationFactor cellFactor = account.getCellFactor();
                if (cellFactor.isValidated()) {
                    resultEvent = new AccountValidatedEvent(ctx);
                } else {
                    resultEvent = new CellValidationEvent(ctx, cellFactor);
                }
            } else {
                resultEvent = new EmailValidationEvent(ctx, emailFactor);
            }
        } catch (Exception e) {
            resultEvent = new InvalidInteractionErrorEvent(ctx, new WarnLoggedResultMeta(RMReg.UNEXPECTED_INTERNAL_ERROR, e));
        }

        // Determine if there are any factors that have to be verified and if so send the event

        // punt for now
        return resultEvent;
    }
}
