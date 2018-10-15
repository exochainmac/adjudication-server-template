package com.exochain.api.bc.fsm.resultready;

import com.exochain.api.bc.fsm.ViewState;

public class ResultReadyState extends ViewState {

    public static final String RESULT_READY_VIEW_NAME = "/api/bc/mfl/returnResult";

    public ResultReadyState() {
        super("resultReadyState", RESULT_READY_VIEW_NAME);
    }
}
