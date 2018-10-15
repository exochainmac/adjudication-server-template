package com.exochain.api.bc.persistence.entities;

import java.io.Serializable;
import java.util.Objects;

public class LoginChannelAccountId implements Serializable {
    private String channelId;
    private String exoId;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getExoId() {
        return exoId;
    }

    public void setExoId(String exoId) {
        this.exoId = exoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginChannelAccountId that = (LoginChannelAccountId) o;
        return Objects.equals(channelId, that.channelId) &&
                Objects.equals(exoId, that.exoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelId, exoId);
    }
}
