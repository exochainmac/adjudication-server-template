package com.exochain.api.bc.domain;

import com.google.common.base.MoreObjects;

public class AuthenticationFactor {

    private final String type;
    private final String otpAddress;
    private final boolean validated;

    public enum AuthType {
        EMAIL,
        SMS
    }

    public AuthenticationFactor(String type, String otpAddress, boolean validated) {
        this.type = type;
        this.otpAddress = otpAddress;
        this.validated = validated;
    }

    public String getType() {
        return type;
    }

    public String getOtpAddress() {
        return otpAddress;
    }

    public boolean isValidated() {
        return validated;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("otpAddress", otpAddress)
                .add("validated", validated)
                .toString();
    }
}
