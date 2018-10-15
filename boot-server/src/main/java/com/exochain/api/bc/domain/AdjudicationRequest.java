package com.exochain.api.bc.domain;

public class AdjudicationRequest {

    private String token;

    private final String callerIPAddress;

    private final String transactionId;

    private String otp;

    public AdjudicationRequest(String token, String callerIPAddress, String transactionId) {
        this(token, callerIPAddress, null, transactionId);
    }

    public AdjudicationRequest(String token, String callerIPAddress, String otp, String transactionId) {
        this.token = token;
        this.callerIPAddress = callerIPAddress;
        this.otp = otp;
        this.transactionId = transactionId;
    }

    public String getToken() {
        return token;
    }

    public String getCallerIPAddress() {
        return callerIPAddress;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
