package com.exochain.api.bc.domain;

public class Account {

    private String id;
    private AuthenticationFactor emailFactor;
    private AuthenticationFactor cellFactor;
    private String userId;
    private Long version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AuthenticationFactor getEmailFactor() {
        return emailFactor;
    }

    public void setEmailFactor(AuthenticationFactor emailFactor) {
        this.emailFactor = emailFactor;
    }

    public AuthenticationFactor getCellFactor() {
        return cellFactor;
    }

    public void setCellFactor(AuthenticationFactor cellFactor) {
        this.cellFactor = cellFactor;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
