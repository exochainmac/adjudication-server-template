package com.exochain.api.bc.config;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "clients.bluecloud")
@Component
public class ApiAccountSettings {
    private String exoApiUrl;
    private String exoJwtAudience;
    private int exoJwtExpirationSecs;
    private String clientPostbackUrl;
    private String clientIssuer;
    private String clientAudience;
    private String publicKeysLocation;
    private String privateKeysLocation;

    public ApiAccountSettings() {
    }

    public String getExoApiUrl() {
        return exoApiUrl;
    }

    public void setExoApiUrl(String exoApiUrl) {
        this.exoApiUrl = exoApiUrl;
    }

    public String getExoJwtAudience() {
        return exoJwtAudience;
    }

    public void setExoJwtAudience(String exoJwtAudience) {
        this.exoJwtAudience = exoJwtAudience;
    }

    public int getExoJwtExpirationSecs() {
        return exoJwtExpirationSecs;
    }

    public void setExoJwtExpirationSecs(int exoJwtExpirationSecs) {
        this.exoJwtExpirationSecs = exoJwtExpirationSecs;
    }

    public String getClientPostbackUrl() {
        return clientPostbackUrl;
    }

    public void setClientPostbackUrl(String clientPostbackUrl) {
        this.clientPostbackUrl = clientPostbackUrl;
    }

    public String getPublicKeysLocation() {
        return publicKeysLocation;
    }

    public void setPublicKeysLocation(String publicKeysLocation) {
        this.publicKeysLocation = publicKeysLocation;
    }

    public String getPrivateKeysLocation() {
        return privateKeysLocation;
    }

    public void setPrivateKeysLocation(String privateKeysLocation) {
        this.privateKeysLocation = privateKeysLocation;
    }

    public String getClientIssuer() {
        return clientIssuer;
    }

    public void setClientIssuer(String clientIssuer) {
        this.clientIssuer = clientIssuer;
    }

    public String getClientAudience() {
        return clientAudience;
    }

    public void setClientAudience(String clientAudience) {
        this.clientAudience = clientAudience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiAccountSettings that = (ApiAccountSettings) o;
        return exoJwtExpirationSecs == that.exoJwtExpirationSecs &&
                Objects.equal(exoApiUrl, that.exoApiUrl) &&
                Objects.equal(exoJwtAudience, that.exoJwtAudience) &&
                Objects.equal(clientPostbackUrl, that.clientPostbackUrl) &&
                Objects.equal(clientIssuer, that.clientIssuer) &&
                Objects.equal(clientAudience, that.clientAudience) &&
                Objects.equal(publicKeysLocation, that.publicKeysLocation) &&
                Objects.equal(privateKeysLocation, that.privateKeysLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(exoApiUrl, exoJwtAudience, exoJwtExpirationSecs, clientPostbackUrl, clientIssuer, clientAudience, publicKeysLocation, privateKeysLocation);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("exoApiUrl", exoApiUrl)
                .add("exoJwtAudience", exoJwtAudience)
                .add("exoJwtExpirationSecs", exoJwtExpirationSecs)
                .add("clientPostbackUrl", clientPostbackUrl)
                .add("clientIssuer", clientIssuer)
                .add("clientAudience", clientAudience)
                .add("publicKeysLocation", publicKeysLocation)
                .add("privateKeysLocation", privateKeysLocation)
                .toString();
    }

}
