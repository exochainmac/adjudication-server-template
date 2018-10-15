package com.exochain.api.bc.fsm.exception;

import com.exochain.result.exception.ExoStructuredException;
import com.exochain.result.meta.DynamicResultMeta;

public class PhoneNumberFormatException extends ExoStructuredException {
    public PhoneNumberFormatException(DynamicResultMeta resultMeta) {
        super(resultMeta);
    }

    public PhoneNumberFormatException(DynamicResultMeta resultMeta, Throwable cause) {
        super(resultMeta, cause);
    }
}
