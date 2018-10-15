package com.exochain.api.bc.form;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class VerifyEmailForm {
    private String emailAddress;
    private String otp;

    public VerifyEmailForm(String emailAddress, String otp) {
        this.emailAddress = emailAddress;
        this.otp = otp;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerifyEmailForm that = (VerifyEmailForm) o;
        return Objects.equal(emailAddress, that.emailAddress) &&
                Objects.equal(otp, that.otp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(emailAddress, otp);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("emailAddress", emailAddress)
                .add("otp", otp)
                .toString();
    }
}
