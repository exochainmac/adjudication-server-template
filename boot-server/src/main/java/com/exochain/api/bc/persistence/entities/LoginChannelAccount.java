package com.exochain.api.bc.persistence.entities;

import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.PersonalData;

import javax.persistence.*;

@Entity
@IdClass(LoginChannelAccountId.class)
@Table(name = "login_channel_account")
public class LoginChannelAccount {

    @Id
    @Column(name = "channel_id")
    private String channelId;

    @DataSubjectId
    @Id
    @Column(name = "exo_id")
    private String exoId;

    @Column(name = "channel_user_id")
    private String channelUserId;

    // Encrypt password with the key tied to their exoId above
    @PersonalData
    private String password;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(String channelUserId) {
        this.channelUserId = channelUserId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExoId() {
        return exoId;
    }

    public void setExoId(String exoId) {
        this.exoId = exoId;
    }
}
