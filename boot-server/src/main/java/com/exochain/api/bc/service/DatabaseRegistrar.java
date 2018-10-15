package com.exochain.api.bc.service;

import com.exochain.api.bc.domain.Account;
import com.exochain.api.bc.domain.AuthenticationFactor;
import com.exochain.api.bc.persistence.entities.ExoAccount;
import com.exochain.api.bc.persistence.entities.LoginChannelAccount;
import com.exochain.api.bc.persistence.repositories.ExoAccountRepository;
import com.exochain.api.bc.persistence.repositories.LoginChannelRepository;
import com.exochain.api.bc.persistence.repositories.LoginChannelAccountRepository;
import com.exochain.result.exception.ExoStructuredException;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.google.common.hash.Hashing;
import io.axoniq.gdpr.api.FieldEncrypter;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import org.axonframework.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class DatabaseRegistrar implements Registrar {

    private static final TimeBasedGenerator UUID_GENERATOR = Generators.timeBasedGenerator();

    private static final Logger L = LoggerFactory.getLogger(DatabaseRegistrar.class);

    private final ExoAccountRepository exoAccounts;
    private final LoginChannelRepository loginChannels;
    private final LoginChannelAccountRepository loginCredentials;

    // FieldEncrypter is how we use Axon GDPR module in regular JPA
    private final FieldEncrypter fieldEncrypter;


    /**
     * CryptoEngine and Serializer are created in AxonConfig and used here to
     * create the encrypter to support the database encryption.
     */
    @Autowired
    public DatabaseRegistrar(ExoAccountRepository exoAccounts, LoginChannelRepository loginChannels,
                             LoginChannelAccountRepository loginCredentials,
                             CryptoEngine cryptoEngine,
                             Serializer serializer) {
        this.exoAccounts = exoAccounts;
        this.loginChannels = loginChannels;
        this.loginCredentials = loginCredentials;
        this.fieldEncrypter = new FieldEncrypter(cryptoEngine, serializer);
    }

    @Override
    public Account lookupByChannelId(String channelId, String channelUserId) throws ExoStructuredException {
        Account account = null;

        LoginChannelAccount credentials = loginCredentials.findByChannelIdAndChannelUserId(channelId, channelUserId);

        if (credentials != null) {
            fieldEncrypter.decrypt(credentials);

            ExoAccount exData = exoAccounts.findOne(credentials.getExoId());

            if (null != exData) {
                fieldEncrypter.decrypt(exData);
            }

            account = toAccount(exData);
        }

        return account;
    }

    @Override
    public Account lookupByUserId(String userId) {
        String userIdHash = hashUserId(userId);

        ExoAccount exData = exoAccounts.findByUserIdHash(userIdHash);

        if (null != exData) {
            fieldEncrypter.decrypt(exData);
        }

        return toAccount(exData);
    }

    @Override
    public Account addAccountToChannel(String channelId, String channelUserId, Account account) {
        L.debug("Adding account [{}] to channel [{}] with channelUserId [{}]", account.getId(), channelId, channelUserId);

        LoginChannelAccount credentials = new LoginChannelAccount();

        credentials.setChannelId(channelId);
        credentials.setChannelUserId(channelUserId);
        credentials.setExoId(account.getId());
        // Just set a random password for now to test encryption
        credentials.setPassword(UUID_GENERATOR.generate().toString());

        fieldEncrypter.encrypt(credentials);
        loginCredentials.save(credentials);

        return account;
    }

    @Override
    public Account create(Account account) {
        return save(account);
    }

    @Override
    public Account update(Account account) {
        return save(account);
    }

    private Account save(Account account) {
        L.debug("Saving account [{}]", account.getId());
        ExoAccount acctData = toExoAccount(account);

        fieldEncrypter.encrypt(acctData);
        ExoAccount saved = exoAccounts.save(acctData);
        account.setVersion(saved.getDataVersion());

        return account;
    }

    private Account toAccount(final ExoAccount exData) {
        if (null == exData) {
            return null;
        }

        Account account = new Account();
        account.setId(exData.getId());
        account.setUserId(exData.getUserId());
        AuthenticationFactor emailFactor = new AuthenticationFactor(
                AuthenticationFactor.AuthType.EMAIL.name(),
                exData.getPrimaryEmail(),
                exData.isEmailVerified());
        account.setEmailFactor(emailFactor);

        AuthenticationFactor cellFactor = new AuthenticationFactor(
                AuthenticationFactor.AuthType.SMS.name(),
                exData.getPrimaryCell(),
                exData.isCellVerified());
        account.setCellFactor(cellFactor);
        account.setVersion(exData.getDataVersion());

        return account;
    }

    private ExoAccount toExoAccount(final Account account) {
        ExoAccount acctData = new ExoAccount();
        acctData.setId(account.getId());
        acctData.setDataVersion(account.getVersion());

        AuthenticationFactor emailFactor = account.getEmailFactor();
        acctData.setPrimaryEmail(emailFactor.getOtpAddress());
        acctData.setEmailVerified(false);

        AuthenticationFactor cellFactor = account.getCellFactor();
        acctData.setPrimaryCell(cellFactor.getOtpAddress());
        acctData.setCellVerified(false);

        acctData.setUserId(account.getUserId().toLowerCase());
        acctData.setUserIdHash(hashUserId(acctData.getUserId()));
        return acctData;
    }

    private String hashUserId(final String userId) {
        return Hashing.sha256().hashString(userId, StandardCharsets.UTF_8).toString();
    }
}
