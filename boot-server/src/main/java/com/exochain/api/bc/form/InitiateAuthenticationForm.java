package com.exochain.api.bc.form;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class InitiateAuthenticationForm implements Serializable {

    @NotNull
    // TODO: Fix minimum token length after validation is set up properly
    @Size(min = 1)
    private String initToken;

    public InitiateAuthenticationForm() {
        this.initToken = StringUtils.EMPTY;
    }

    public InitiateAuthenticationForm(String initToken) {
        this.initToken = initToken;
    }

    public String getInitToken() {
        return initToken;
    }

    public void setInitToken(String initToken) {
        this.initToken = initToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InitiateAuthenticationForm that = (InitiateAuthenticationForm) o;
        return Objects.equal(initToken, that.initToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(initToken);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("initToken", initToken)
                .toString();
    }
}
