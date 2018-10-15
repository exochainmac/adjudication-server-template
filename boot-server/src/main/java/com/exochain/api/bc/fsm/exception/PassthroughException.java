package com.exochain.api.bc.fsm.exception;

import com.exochain.result.exception.ExoStructuredException;
import com.exochain.result.meta.DynamicResultMeta;

public class PassthroughException extends ExoStructuredException {
    public PassthroughException(DynamicResultMeta resultMeta) {
        super(resultMeta);
    }

    public PassthroughException(DynamicResultMeta resultMeta, Throwable cause) {
        super(resultMeta, cause);
    }
}
