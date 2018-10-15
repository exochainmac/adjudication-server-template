package com.exochain.api.bc.fsm.catastrophicerror;

import com.exochain.api.bc.fsm.ViewState;

public class CatastrophicErrorState extends ViewState {

    public static final String INVALID_INTERACTION_VIEW_NAME = "/api/bc/mfl/catastrophicError";

    public CatastrophicErrorState() {
        super("catastrophicErrorState", INVALID_INTERACTION_VIEW_NAME);
    }
}
