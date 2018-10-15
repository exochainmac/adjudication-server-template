package com.exochain.api.bc;

import com.exochain.ap.auth.ApAuthenticationManager;
import com.exochain.api.bc.config.ApiAccountSettings;
import com.exochain.api.bc.domain.AdjudicationRequest;
import com.exochain.api.bc.form.InitiateAuthenticationForm;
import com.exochain.api.bc.form.VerifyEmailForm;
import com.exochain.api.bc.form.VerifyOtpForm;
import com.exochain.api.bc.fsm.BcLoginContext;
import com.exochain.api.bc.fsm.StandardBcLoginContext;
import com.exochain.api.bc.fsm.ExecutionState;
import com.exochain.api.bc.fsm.FsmFactory;
import com.exochain.api.bc.fsm.ViewState;
import com.exochain.api.bc.fsm.cellvalidated.CellValidatedEvent;
import com.exochain.api.bc.fsm.emailvalidated.EmailValidatedEvent;
import com.exochain.api.bc.fsm.rawreceived.RawTokenReceivedEvent;
import com.exochain.api.bc.service.DatabaseRegistrar;
import com.exochain.api.bc.service.Registrar;
import com.exochain.jwt.processing.TokenProcessor;
import com.exochain.result.exception.ExoStructuredException;
import com.exochain.result.meta.ErrorLoggedResultMeta;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import org.jeasy.states.api.Event;
import org.jeasy.states.api.FiniteStateMachine;
import org.jeasy.states.api.FiniteStateMachineException;
import org.jeasy.states.api.State;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Stream;

@Controller
@RequestMapping("/api/bc")
@SessionAttributes(names = {"bcLoginContext"})
public class BCController {

    private static final XLogger L = XLoggerFactory.getXLogger(BCController.class);

    private final TokenProcessor<SimpleSecurityContext> jwTokenProcessor;
    private final Registrar registrar;
    private final FsmFactory fsmFactory;
    private final ApiAccountSettings apiAccountSettings;
    private final ApAuthenticationManager apManager;

    @Autowired
    public BCController(TokenProcessor<SimpleSecurityContext> tokenProcessor,
                        DatabaseRegistrar registrar,
                        FsmFactory fsmFactory,
                        ApiAccountSettings apiAccountSettings,
                        ApAuthenticationManager apManager) {
        this.jwTokenProcessor = tokenProcessor;
        this.registrar = registrar;
        this.fsmFactory = fsmFactory;
        this.apiAccountSettings = apiAccountSettings;
        this.apManager = apManager;
    }

    @PostMapping(path = "/mfl", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String processMultifactorLogin(@Valid @ModelAttribute("command") final InitiateAuthenticationForm form,
                                          final BindingResult bindingResult,
                                          final ModelMap modelMap,
                                          final HttpServletRequest httpRequest) {
        L.entry(form, bindingResult, modelMap);

        if (bindingResult.hasErrors()) {
            // TODO: Set up structured response handling and build into return token
            L.info("CLIENTERRROR: Validation errors: [{}]", bindingResult);
        }

        AdjudicationRequest request = new AdjudicationRequest(form.getInitToken(), determineClientIPAddress(httpRequest), "");
/*
        Adjudication adjudication = adjudicator.adjudicate(request);

        modelMap.put("initToken", form.getInitToken());

        switch (adjudication.getAttestation().getResult()) {
            case OTP_REQUIRED:
                return L.exit("/api/bc/mfl/otpVerification");
            default:
                return L.exit("/api/bc/mfl/authFinalize");
        }
*/
    return L.exit("Not implemented");
    }

    private String determineClientIPAddress(HttpServletRequest httpRequest) {
        Optional<String> optionalIPAddressHeader = Stream.of(
                "X-Client-Ip",
                "x-forwarded-for",
                "X-FORWARDED",
                "FORWARDED-FOR",
                "FORWARDED",
                "REMOTE-ADDR",
                "Proxy-Client-IP",
                "WL-Proxy-Clint-IP",
                "CLIENT-IP"
        ).filter(header -> containsAValidIPAddress(httpRequest.getHeader(header))).findFirst();

        if (optionalIPAddressHeader.isPresent()) {
            String header = optionalIPAddressHeader.get();
            return httpRequest.getHeader(header);
        } else {
            String ipAddress = httpRequest.getRemoteAddr();
            return containsAValidIPAddress(ipAddress) ? ipAddress : null;
        }
    }

    private boolean containsAValidIPAddress(String value) {
        return value != null && value.trim().length() > 0 &&
                !"unknown".equalsIgnoreCase(value) &&
                !value.contains(".0.") &&
                !value.contains(":0:");
    }

    @PostMapping(path = "/mfl/verifyOtp", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String verifyOtp(@Valid @ModelAttribute("command") final VerifyOtpForm form,
                            final BindingResult bindingResult,
                            final ModelMap modelMap,
                            final HttpServletRequest httpRequest) {
        L.entry(form, bindingResult, modelMap);

        if (bindingResult.hasErrors()) {
            // TODO: Set up structured response handling and build into return token
            L.info("CLIENTERRROR: Validation errors: [{}]", bindingResult);
        }

        AdjudicationRequest request = new AdjudicationRequest(form.getInitToken(),
                determineClientIPAddress(httpRequest), form.getOtp(), "");
/*
        Adjudication adjudication = adjudicator.adjudicate(request);

        modelMap.put("initToken", form.getInitToken());

        switch (adjudication.getAttestation().getResult()) {
            case INVALID_OTP:
            case EXPIRED_OTP:
                return L.exit("/api/bc/mfl/otpVerification");
            default:
                return L.exit("/api/bc/mfl/authFinalize");
        }
*/
        return L.exit("Not Implemented");
    }


    @ModelAttribute("bcLoginContext")
    public BcLoginContext initializeBcLoginContext() {
        L.entry();
        StandardBcLoginContext ctx = new StandardBcLoginContext(fsmFactory.createFsm(), jwTokenProcessor, registrar, apManager);
        // Initially assume success
        ctx.setResultMeta(RMReg.REQUEST_SUCCEEDED);
        // TODO: For future clients, make this dynamically set upon the initToken post instead of here
        ctx.setApiAccountSettings(apiAccountSettings);
        return L.exit(ctx);
    }


    @PostMapping(path = "/mfltest", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String fireTokenInput(@ModelAttribute("bcLoginContext") BcLoginContext ctx,
                                 @Valid @ModelAttribute("command") final InitiateAuthenticationForm form,
                                 final BindingResult bindingResult,
                                 final ModelMap modelMap,
                                 final HttpServletRequest httpRequest) {
        return sendEvent(ctx, new RawTokenReceivedEvent(ctx, form.getInitToken()));
    }

    @GetMapping(path = "/mfltest/forceemailauth")
    public String forceEmailAuth(@ModelAttribute("bcLoginContext") BcLoginContext ctx) {
        return sendEvent(ctx, new EmailValidatedEvent(ctx));
    }

    @PostMapping(path = "/mfltest/emailauth", params = "sendEmail", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String performEmailAuth(@ModelAttribute("bcLoginContext") BcLoginContext ctx,
                                   @ModelAttribute("verifyEmailForm") VerifyEmailForm emailForm) {
        L.entry(emailForm);
        return "/api/bc/mfl/validateEmail";
    }

    @GetMapping(path = "/mfltest/forcecellauth")
    public String forceCellAuth(@ModelAttribute("bcLoginContext") BcLoginContext ctx) {
        return sendEvent(ctx, new CellValidatedEvent(ctx));
    }

    private String sendEvent(BcLoginContext ctx, Event evt) {
        try {
            return handleEvent(ctx, evt);
        } catch (Exception e) {
            if(e.getCause() instanceof ExoStructuredException) {
                L.info("Caught exo exception");
            } else {
                L.error("Caught some other exception", e);
                return "processingError";
            }
        }

        //TODO: proper error handling
        return "hello";
    }

    private String handleEvent(final BcLoginContext ctx, final Event firstEvent) throws ExoStructuredException {
        Event nextEvent = firstEvent;
        FiniteStateMachine fsm = ctx.getFsm();
        while(null != nextEvent) {
            try {
                State priorState = fsm.getCurrentState();
                L.debug("Firing event: {}", nextEvent.getName());
                fsm.fire(nextEvent);
                State currentState = fsm.getCurrentState();

                requireNonDuplicateExecutionState(priorState, currentState);

                L.debug("Executing state: {}", currentState.getName());
                if(currentState instanceof ViewState) {
                    return ((ViewState)currentState).getViewName();
                } else if (currentState instanceof ExecutionState) {
                    nextEvent = ((ExecutionState)currentState).stateAction(ctx);
                } else {
                    //TODO: fix me to handle error properly, probably by setting nextEvent to passthrougherror
                    throw new IllegalStateException("Invalidate state class");
                }
            } catch (FiniteStateMachineException e) {
                //TODO: fix me to translate to structured exception
                L.error("BAD THING", e);
            }
        }
        // TODO: Fix this
        return "processingError";
    }

    private void requireNonDuplicateExecutionState(State oldState, State newState) throws ExoStructuredException {
        // If the old and new state are the same, and are an executable state then we are stuck in a loop
        if(newState.equals(oldState) && (newState instanceof ExecutionState)) {
            throw new ExoStructuredException(new ErrorLoggedResultMeta(RMReg.UNEXPECTED_INTERNAL_ERROR, "State loop"));
        }
    }
}
