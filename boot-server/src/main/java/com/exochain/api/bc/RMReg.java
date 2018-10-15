package com.exochain.api.bc;

import com.exochain.result.meta.ErrorSource;
import com.exochain.result.meta.ResultMetaDynEnum;

import java.io.Serializable;

public class RMReg extends ResultMetaDynEnum<RMReg> implements Serializable {
    public static final String DEFAULT_NAME_SPACE = "ExoMultiFactorDemo";
    private static final long serialVersionUID = 1774668073169613350L;
    private static final boolean SUCCESS = true;
    private static final boolean FAILURE = false;

    public static final RMReg REQUEST_SUCCEEDED =
            new RMReg(2001000, SUCCESS, "REQUEST_SUCCEEDED", "The request succeeded", ErrorSource.NONE, false);
    // ------ 400's
    public static final RMReg ANON_TRAFFIC_NOT_ALLOWED_ERROR =
            new RMReg(4001000, FAILURE, "ANON_TRAFFIC_NOT_ALLOWED_ERROR", "Anonymous clients not allowed", ErrorSource.CLIENT, false);

    // -- 422's
    public static final RMReg CLAIM_PARSING_ERROR =
            new RMReg(4221000, FAILURE, "CLAIM_PARSING_ERROR", "Error parsing JWT claim", ErrorSource.CLIENT, false);

    public static final RMReg CLAIM_CONTENT_ERROR =
            new RMReg(4221001, FAILURE, "CLAIM_CONTENT_ERROR", "Content in a claim was invalid", ErrorSource.CLIENT, false);


    // -- 500's
    public static final RMReg UNEXPECTED_INTERNAL_ERROR =
            new RMReg(5001003, FAILURE, "UNEXPECTED_INTERNAL_ERROR", "Unexpected internal error", ErrorSource.SERVER, false);


    private RMReg(int code, boolean success, String nameSpace, String friendlyName, String description, ErrorSource blame, boolean retryable) {
        super(code, success, nameSpace, friendlyName, description, blame, retryable);
    }

    private RMReg(int code, boolean success, String friendlyName, String description, ErrorSource blame, boolean retryable) {
        super(code, success, DEFAULT_NAME_SPACE, friendlyName, description, blame, retryable);
    }

    private RMReg() {

    }

    public static <E> ResultMetaDynEnum<? extends ResultMetaDynEnum<?>>[] values() {
        return values(RMReg.class);
    }
}
