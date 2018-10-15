package com.exochain.api.bc.fsm;

import com.exochain.result.exception.ExoStructuredException;
import com.exochain.result.meta.DynamicResultMeta;

public class InvalidEventTypeException extends ExoStructuredException {
    public InvalidEventTypeException(DynamicResultMeta resultMeta) {
        super(resultMeta);
    }

    public InvalidEventTypeException(DynamicResultMeta resultMeta, Throwable cause) {
        super(resultMeta, cause);
    }
}
