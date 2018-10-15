package com.exochain.api.bc.domain;

import com.nimbusds.jwt.JWTClaimsSet;

public class Attestation {

    public enum Result {
        CONFIRMED,
        OTP_REQUIRED,
        INVALID_OTP,
        EXPIRED_OTP
    }

    private JWTClaimsSet attestedClaims;

    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public JWTClaimsSet getAttestedClaims() {
        return attestedClaims;
    }

    public void setAttestedClaims(JWTClaimsSet attestedClaims) {
        this.attestedClaims = attestedClaims;
    }
}
