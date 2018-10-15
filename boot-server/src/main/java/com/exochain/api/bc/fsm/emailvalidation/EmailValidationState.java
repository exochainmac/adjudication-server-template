package com.exochain.api.bc.fsm.emailvalidation;

import com.exochain.api.bc.fsm.ViewState;

public class EmailValidationState extends ViewState {

    public static final String VIEW_NAME = "/api/bc/mfl/validateEmail";

    public EmailValidationState() {
        super(EmailValidationState.class.getSimpleName(), VIEW_NAME);
    }
}
