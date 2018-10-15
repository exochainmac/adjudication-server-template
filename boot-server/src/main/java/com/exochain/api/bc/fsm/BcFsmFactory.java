package com.exochain.api.bc.fsm;

import com.exochain.api.bc.config.ApiAccountSettings;
import com.exochain.api.bc.fsm.acctevaluation.AccountEvaluationEvent;
import com.exochain.api.bc.fsm.acctevaluation.AccountEvaluationHandler;
import com.exochain.api.bc.fsm.acctevaluation.AccountEvaluationState;
import com.exochain.api.bc.fsm.acctfound.AccountFoundEvent;
import com.exochain.api.bc.fsm.acctfound.AccountFoundHandler;
import com.exochain.api.bc.fsm.acctfound.AccountFoundState;
import com.exochain.api.bc.fsm.acctinitiation.AccountInitiationEvent;
import com.exochain.api.bc.fsm.acctinitiation.AccountInitiationHandler;
import com.exochain.api.bc.fsm.acctinitiation.AccountInitiationState;
import com.exochain.api.bc.fsm.acctnotfound.AccountNotFoundEvent;
import com.exochain.api.bc.fsm.acctnotfound.AccountNotFoundHandler;
import com.exochain.api.bc.fsm.acctnotfound.AccountNotFoundState;
import com.exochain.api.bc.fsm.acctvalidated.AccountValidatedEvent;
import com.exochain.api.bc.fsm.acctvalidated.AccountValidatedState;
import com.exochain.api.bc.fsm.authsucceeded.AuthSucceededEvent;
import com.exochain.api.bc.fsm.authsucceeded.AuthSucceededState;
import com.exochain.api.bc.fsm.bypasserror.BypassErrorEvent;
import com.exochain.api.bc.fsm.bypasserror.BypassErrorHandler;
import com.exochain.api.bc.fsm.bypasserror.BypassErrorState;
import com.exochain.api.bc.fsm.catastrophicerror.CatastrophicErrorEvent;
import com.exochain.api.bc.fsm.catastrophicerror.CatastrophicErrorState;
import com.exochain.api.bc.fsm.cellvalidated.CellValidatedEvent;
import com.exochain.api.bc.fsm.cellvalidated.CellValidatedHandler;
import com.exochain.api.bc.fsm.cellvalidated.CellValidatedState;
import com.exochain.api.bc.fsm.chnlacctinitiation.ChannelAccountInitiationEvent;
import com.exochain.api.bc.fsm.chnlacctinitiation.ChannelAccountInitiationHandler;
import com.exochain.api.bc.fsm.chnlacctinitiation.ChannelAccountInitiationState;
import com.exochain.api.bc.fsm.claimsparsed.JwtClaimsParsedEvent;
import com.exochain.api.bc.fsm.claimsparsed.JwtClaimsParsedHandler;
import com.exochain.api.bc.fsm.claimsparsed.JwtClaimsParsedState;
import com.exochain.api.bc.fsm.emailvalidated.EmailValidatedEvent;
import com.exochain.api.bc.fsm.emailvalidated.EmailValidatedHandler;
import com.exochain.api.bc.fsm.emailvalidated.EmailValidatedState;
import com.exochain.api.bc.fsm.emailvalidation.EmailValidationHandler;
import com.exochain.api.bc.fsm.invalidinteractionerror.InvalidInteractionErrorEvent;
import com.exochain.api.bc.fsm.invalidinteractionerror.InvalidInteractionErrorState;
import com.exochain.api.bc.fsm.rawreceived.RawTokenReceivedEvent;
import com.exochain.api.bc.fsm.rawreceived.RawTokenReceivedHandler;
import com.exochain.api.bc.fsm.rawreceived.RawTokenReceivedState;
import com.exochain.api.bc.fsm.resultbuilding.ResultBuildingEvent;
import com.exochain.api.bc.fsm.resultbuilding.ResultBuildingState;
import com.exochain.api.bc.fsm.resultready.ResultReadyEvent;
import com.exochain.api.bc.fsm.resultready.ResultReadyHandler;
import com.exochain.api.bc.fsm.resultready.ResultReadyState;
import com.exochain.api.bc.fsm.cellvalidation.CellValidationEvent;
import com.exochain.api.bc.fsm.cellvalidation.CellValidationState;
import com.exochain.api.bc.fsm.emailvalidation.EmailValidationEvent;
import com.exochain.api.bc.fsm.emailvalidation.EmailValidationState;
import com.exochain.api.bc.fsm.waitingraw.WaitingForRawTokenState;
import com.exochain.jwt.processing.TokenEncrypter;
import com.exochain.jwt.processing.TokenSigner;
import org.jeasy.states.api.FiniteStateMachine;
import org.jeasy.states.api.State;
import org.jeasy.states.api.Transition;
import org.jeasy.states.core.FiniteStateMachineBuilder;
import org.jeasy.states.core.TransitionBuilder;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class BcFsmFactory implements FsmFactory {
    private static final XLogger L = XLoggerFactory.getXLogger(BcFsmFactory.class);

    private final TokenEncrypter tokenEncrypter;
    private final TokenSigner tokenSigner;
    private final ApiAccountSettings apiAccountSettings;


    private final State STATE_WAITING_FOR_RAW_TOKEN;
    private final State STATE_RAW_TOKEN_RECEIVED;
    private final State STATE_JWT_CLAIMS_PARSED;

    // Error state where we halt processing and do not pass anything back to BC
    // e.g. when bot hits with invalid tokens, etc.
    private final State STATE_INVALID_INTERACTION_ERROR;
    // Error state where we have internal error and we pass through back to blue cloud
    // so we don't block their users.
    private final State STATE_BYPASS_ERROR;

    // Error state that can occur in the final processing stage where we can't build
    // a response properly.
    private final State STATE_CATASTROPHIC_ERROR;

    private final State STATE_ACCOUNT_FOUND;

    private final State STATE_ACCOUNT_NOT_FOUND;
    
    private final State STATE_ACCOUNT_VALIDATED;

    private final State STATE_ACCOUNT_EVALUATION;

    private final State STATE_ACCOUNT_INITIATION;

    private final State STATE_CHANNEL_ACCOUNT_INITIATION;
    
    private final State STATE_CELL_VALIDATION;

    private final State STATE_CELL_VALIDATED;
    
    private final State STATE_EMAIL_VALIDATION;

    private final State STATE_EMAIL_VALIDATED;

    private final State STATE_AUTH_SUCCEEDED;

    private final State STATE_RESULT_BUILDING;

    private final State STATE_RESULT_READY;

    public BcFsmFactory(TokenSigner tokenSigner, TokenEncrypter tokenEncrypter, ApiAccountSettings apiAccountSettings) {
        this.tokenSigner = tokenSigner;
        this.tokenEncrypter = tokenEncrypter;
        this.apiAccountSettings = apiAccountSettings;

        STATE_WAITING_FOR_RAW_TOKEN = new WaitingForRawTokenState();
        STATE_RAW_TOKEN_RECEIVED = new RawTokenReceivedState();
        STATE_JWT_CLAIMS_PARSED = new JwtClaimsParsedState();

        // Error state where we halt processing and do not pass anything back to BC
        // e.g. when bot hits with invalid tokens, etc.
        STATE_INVALID_INTERACTION_ERROR = new InvalidInteractionErrorState();
        STATE_BYPASS_ERROR = new BypassErrorState();
        STATE_CATASTROPHIC_ERROR = new CatastrophicErrorState();

        STATE_ACCOUNT_FOUND = new AccountFoundState();
        STATE_ACCOUNT_NOT_FOUND = new AccountNotFoundState();
        STATE_ACCOUNT_VALIDATED = new AccountValidatedState();
        STATE_ACCOUNT_EVALUATION = new AccountEvaluationState();
        STATE_ACCOUNT_INITIATION = new AccountInitiationState();
        STATE_CHANNEL_ACCOUNT_INITIATION = new ChannelAccountInitiationState();
        STATE_CELL_VALIDATION = new CellValidationState();
        STATE_CELL_VALIDATED = new CellValidatedState();
        STATE_EMAIL_VALIDATION = new EmailValidationState();
        STATE_EMAIL_VALIDATED = new EmailValidatedState();
        STATE_AUTH_SUCCEEDED = new AuthSucceededState();
        STATE_RESULT_BUILDING = new ResultBuildingState(tokenSigner, tokenEncrypter);
        STATE_RESULT_READY = new ResultReadyState();

    }

    @Override
    public FiniteStateMachine createFsm() {
        L.entry();

        Set<State> states = new HashSet<>();
        Set<Transition> transitions = new HashSet<>();

        // Set up all the state transitions
        fromWaitingForRawToken(states, transitions);
        fromRawTokenReceived(states, transitions);
        fromJwtClaimsParsed(states, transitions);
        fromAccountNotFound(states, transitions);
        fromAccountFound(states, transitions);
        fromAccountInitation(states, transitions);
        fromChannelAccountInitation(states, transitions);
        fromAccountEvaluation(states, transitions);
        fromAccountValidated(states, transitions);
        fromAuthSucceeded(states, transitions);
        fromEmailValidation(states, transitions);
        fromEmailValidated(states, transitions);
        fromCellValidation(states, transitions);
        fromCellValidated(states, transitions);
        fromResultBuilding(states, transitions);
        fromResultReady(states, transitions);
        fromBypassError(states, transitions);
        fromInvalidInteractionError(states, transitions);
        fromCatastrophicError(states, transitions);

        FiniteStateMachine bcFsm = new FiniteStateMachineBuilder(states, STATE_WAITING_FOR_RAW_TOKEN)
                .registerTransitions(transitions)
                .build();

        return bcFsm;
    }

    /**
     * Configures transitions out of STATE_WAITING_FOR_RAW_TOKEN state
     *
     * @param states mutable set of states for the fsm, add yours
     * @param transitions mutable set of transitions for the fsm, add yours
     */
    private void fromWaitingForRawToken(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_WAITING_FOR_RAW_TOKEN;
        fromSourceToRawTokenReceived(states, transitions, myState);
    }

    /**
     * Configures transitions out of the STATE_RAW_TOKEN_RECEIVED state
     *
     * @param states mutable set of states for the fsm, add yours
     * @param transitions mutable set of transitions for the fsm, add yours
     */
    private void fromRawTokenReceived(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_RAW_TOKEN_RECEIVED;
        // First add any states we require in our transitions, order doesn't matter and they
        // may have already been registered, which is okay
        states.add(myState);

        fromSourceToJwtClaimsParsed(states, transitions, myState);

        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromJwtClaimsParsed(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_JWT_CLAIMS_PARSED;
        states.add(myState);

        fromSourceToAccountNotFound(states, transitions, myState);
        fromSourceToAccountFound(states, transitions, myState);
        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromAccountFound(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_ACCOUNT_FOUND;
        states.add(myState);

        fromSourceToAccountEvaluation(states, transitions, myState);
        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromAccountEvaluation(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_ACCOUNT_EVALUATION;
        states.add(myState);

        fromSourceToAccountValidated(states, transitions, myState);
        fromSourceToEmailValidation(states, transitions, myState);
        fromSourceToCellValidation(states, transitions, myState);
        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromAccountValidated(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_ACCOUNT_VALIDATED;
        states.add(myState);
        states.add(STATE_AUTH_SUCCEEDED);

        fromSourceToAuthSucceeded(states, transitions, myState);
        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromEmailValidation(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_EMAIL_VALIDATION;
        states.add(myState);

        fromSourceToEmailValidated(states, transitions, myState);
        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromEmailValidated(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_EMAIL_VALIDATED;
        states.add(myState);

        fromSourceToAccountFound(states, transitions, myState);
        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromCellValidation(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_CELL_VALIDATION;
        states.add(myState);

        fromSourceToCellValidated(states, transitions, myState);
        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromCellValidated(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_CELL_VALIDATED;
        states.add(myState);

        fromSourceToAccountFound(states, transitions, myState);

        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromSourceToCommonStates(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        // Always jump back to token received if a new one comes in for this session
        fromSourceToRawTokenReceived(states, transitions, sourceState);
        fromSourceToInvalidInteractionError(states, transitions, sourceState);
        fromSourceToBypassError(states, transitions, sourceState);
        fromSourceToCatastrophicError(states, transitions, sourceState);
    }

    private void fromAuthSucceeded(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_AUTH_SUCCEEDED;
        states.add(myState);

        fromSourceToResultBuilding(states, transitions, myState);
        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromResultBuilding(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_RESULT_BUILDING;
        states.add(myState);

        fromSourceToResultReady(states, transitions, myState);
        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromResultReady(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_RESULT_READY;
        states.add(myState);

        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromAccountNotFound(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_ACCOUNT_NOT_FOUND;
        states.add(myState);

        fromSourceToAccountInitiation(states, transitions, myState);
        fromSourceToChannelAccountInitiation(states, transitions, myState);

        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromAccountInitation(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_ACCOUNT_INITIATION;
        states.add(myState);

        fromSourceToAccountEvaluation(states, transitions, myState);
        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromChannelAccountInitation(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_CHANNEL_ACCOUNT_INITIATION;
        states.add(myState);

        fromSourceToAccountEvaluation(states, transitions, myState);
        fromSourceToCommonStates(states, transitions, myState);
    }

    private void fromInvalidInteractionError(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_INVALID_INTERACTION_ERROR;
        fromSourceToRawTokenReceived(states, transitions, myState);
    }

    private void fromCatastrophicError(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_CATASTROPHIC_ERROR;
        fromSourceToRawTokenReceived(states, transitions, myState);
    }

    private void fromBypassError(final Set<State> states, final Set<Transition> transitions) {
        State myState = STATE_BYPASS_ERROR;
        states.add(myState);

        fromSourceToResultBuilding(states, transitions, myState);

        // Always jump back to token received if a new one comes in for this session
        fromSourceToRawTokenReceived(states, transitions, myState);
        fromSourceToInvalidInteractionError(states, transitions, myState);
        fromSourceToBypassError(states, transitions, myState);
    }

    /**
     * Sets up a transition from any source state to the halting error state
     * @param states
     * @param transitions
     * @param sourceState
     */
    private void fromSourceToInvalidInteractionError(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_INVALID_INTERACTION_ERROR;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition sourceStateToHalt = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(InvalidInteractionErrorEvent.class)
                .targetState(STATE_INVALID_INTERACTION_ERROR)
                .build();

        transitions.add(sourceStateToHalt);
    }

    /**
     * Sets up a transition from any source state to the bypass error state
     * @param states
     * @param transitions
     * @param sourceState
     */
    private void fromSourceToBypassError(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_BYPASS_ERROR;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition sourceStateToBypass = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(BypassErrorEvent.class)
                .eventHandler(new BypassErrorHandler())
                .targetState(STATE_BYPASS_ERROR)
                .build();

        transitions.add(sourceStateToBypass);
    }

    private void fromSourceToCatastrophicError(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_CATASTROPHIC_ERROR;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(CatastrophicErrorEvent.class)
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToRawTokenReceived(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_RAW_TOKEN_RECEIVED;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(RawTokenReceivedEvent.class)
                .eventHandler(new RawTokenReceivedHandler())
                .targetState(STATE_RAW_TOKEN_RECEIVED)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToAccountFound(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_ACCOUNT_FOUND;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(AccountFoundEvent.class)
                .eventHandler(new AccountFoundHandler())
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToResultBuilding(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_RESULT_BUILDING;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(ResultBuildingEvent.class)
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToAuthSucceeded(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_AUTH_SUCCEEDED;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(AuthSucceededEvent.class)
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToJwtClaimsParsed(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_JWT_CLAIMS_PARSED;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(JwtClaimsParsedEvent.class)
                .eventHandler(new JwtClaimsParsedHandler())
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToAccountNotFound(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_ACCOUNT_NOT_FOUND;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(AccountNotFoundEvent.class)
                .eventHandler(new AccountNotFoundHandler())
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToAccountValidated(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_ACCOUNT_VALIDATED;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(AccountValidatedEvent.class)
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToAccountEvaluation(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_ACCOUNT_EVALUATION;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(AccountEvaluationEvent.class)
                .eventHandler(new AccountEvaluationHandler())
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToEmailValidation(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_EMAIL_VALIDATION;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(EmailValidationEvent.class)
                .eventHandler(new EmailValidationHandler())
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToCellValidation(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_CELL_VALIDATION;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(CellValidationEvent.class)
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToEmailValidated(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_EMAIL_VALIDATED;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(EmailValidatedEvent.class)
                .eventHandler(new EmailValidatedHandler())
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToCellValidated(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_CELL_VALIDATED;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(CellValidatedEvent.class)
                .eventHandler(new CellValidatedHandler())
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToResultReady(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_RESULT_READY;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(ResultReadyEvent.class)
                .eventHandler(new ResultReadyHandler())
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToAccountInitiation(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_ACCOUNT_INITIATION;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(AccountInitiationEvent.class)
                .eventHandler(new AccountInitiationHandler())
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private void fromSourceToChannelAccountInitiation(final Set<State> states, final Set<Transition> transitions, State sourceState) {
        State myState = STATE_CHANNEL_ACCOUNT_INITIATION;
        states.add(sourceState);
        states.add(myState);

        // Now configure any transitions out of this state to others
        Transition transition = new TransitionBuilder()
                .name(buildTransitionName(sourceState, myState))
                .sourceState(sourceState)
                .eventType(ChannelAccountInitiationEvent.class)
                .eventHandler(new ChannelAccountInitiationHandler())
                .targetState(myState)
                .build();

        transitions.add(transition);
    }

    private static String buildTransitionName(State sourceState, State targetState) {
        return sourceState.getName() + "To" + targetState.getName();
    }

}
