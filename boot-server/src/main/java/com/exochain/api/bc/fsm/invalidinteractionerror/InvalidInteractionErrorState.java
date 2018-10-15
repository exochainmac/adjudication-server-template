package com.exochain.api.bc.fsm.invalidinteractionerror;

import com.exochain.api.bc.fsm.ViewState;

public class InvalidInteractionErrorState extends ViewState {

    public static final String INVALID_INTERACTION_VIEW_NAME = "/api/bc/mfl/invalidInteraction";

    public InvalidInteractionErrorState() {
        super("invalidInteractionErrorState", INVALID_INTERACTION_VIEW_NAME);
    }
}
