package com.exochain.api.bc.form;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class VerifyOtpForm implements Serializable {

    @NotNull
    // TODO: Fix minimum token length after validation is set up properly
    @Size(min = 1)
    private String initToken;

    @NotNull
    // TODO: Fix minimum token length after validation is set up properly
    @Size(min = 1)
    private String otp;

    public VerifyOtpForm() {
        this.initToken = StringUtils.EMPTY;
    }

    public VerifyOtpForm(String initToken) {
        this.initToken = initToken;
    }

    public String getInitToken() {
        return initToken;
    }

    public void setInitToken(String initToken) {
        this.initToken = initToken;
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
        VerifyOtpForm that = (VerifyOtpForm) o;
        return java.util.Objects.equals(initToken, that.initToken) &&
                java.util.Objects.equals(otp, that.otp);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(initToken, otp);
    }

    @Override
    public String toString() {
        return "VerifyOtpForm{" +
                "initToken='" + initToken + '\'' +
                ", otp='" + otp + '\'' +
                '}';
    }
}
