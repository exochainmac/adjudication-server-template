package com.exochain.api.bc.fsm.cellvalidation;

import com.exochain.api.bc.fsm.ViewState;

public class CellValidationState extends ViewState {

    public static final String VIEW_NAME = "/api/bc/mfl/validateCell";

    public CellValidationState() {
        super(CellValidationState.class.getSimpleName(), VIEW_NAME);
    }
}
