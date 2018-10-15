package com.exochain.api.bc.domain;

import com.nimbusds.jwt.JWTClaimsSet;

public class Adjudication {

    private int score;

    private JWTClaimsSet unsubstantiatedClaims;

    private JWTClaimsSet attestedClaims;

    private Attestation attestation;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public JWTClaimsSet getUnsubstantiatedClaims() {
        return unsubstantiatedClaims;
    }

    public void setUnsubstantiatedClaims(JWTClaimsSet unsubstantiatedClaims) {
        this.unsubstantiatedClaims = unsubstantiatedClaims;
    }

    public JWTClaimsSet getAttestedClaims() {
        return attestedClaims;
    }

    public void setAttestedClaims(JWTClaimsSet attestedClaims) {
        this.attestedClaims = attestedClaims;
    }

    public Attestation getAttestation() {
        return attestation;
    }

    public void setAttestation(Attestation attestation) {
        this.attestation = attestation;
    }
}
