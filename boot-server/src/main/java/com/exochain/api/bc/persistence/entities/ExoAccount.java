package com.exochain.api.bc.persistence.entities;

import com.exochain.jpa.base.TraceableEntityBase;
import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.PersonalData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "exo_account",
uniqueConstraints = @UniqueConstraint(columnNames = {"user_id_hash"}))
public class ExoAccount extends TraceableEntityBase {

    // Datasubjectid is the id of the encryption key to use for encrypting this record
    @DataSubjectId
    @Id
    private String id;

    @PersonalData
    private String userId;

    @Column(name = "user_id_hash")
    private String userIdHash;

    @PersonalData
    private String password;

    @PersonalData
    private String primaryEmail;

    private boolean emailVerified;

    @PersonalData
    private String primaryCell;

    private boolean cellVerified;

    @PersonalData
    private String ethereumAccount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getPrimaryCell() {
        return primaryCell;
    }

    public void setPrimaryCell(String primaryCell) {
        this.primaryCell = primaryCell;
    }

    public boolean isCellVerified() {
        return cellVerified;
    }

    public void setCellVerified(boolean cellVerified) {
        this.cellVerified = cellVerified;
    }

    public String getUserIdHash() {
        return userIdHash;
    }

    public void setUserIdHash(String userIdHash) {
        this.userIdHash = userIdHash;
    }

    public String getEthereumAccount() {
        return ethereumAccount;
    }

    public void setEthereumAccount(String ethereumAccount) {
        this.ethereumAccount = ethereumAccount;
    }
}
