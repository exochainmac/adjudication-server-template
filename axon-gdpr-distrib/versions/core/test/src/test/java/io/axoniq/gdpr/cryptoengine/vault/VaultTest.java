/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.cryptoengine.vault;

import com.bettercloud.vault.SslConfig;
import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.api.Auth;
import com.bettercloud.vault.response.AuthResponse;
import io.axoniq.gdpr.cryptoengine.AbstractEngineTestSet;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Tests of the VaultCryptoEngine. The VAULT_ADDRESS and ROOT_TOKEN are hard coded in this test.
 * The simplest way to run this test:
 * - run a dev-mode Vault server on localhost (vault server -dev)
 * - copy the root token from the logging and paste in in the ROOT_TOKEN constant
 *
 * The tests can also be run against a full-fledged Vault server. We're using an OkHttpClient that
 * allows untrusted certs, so it's also possible to run the tests again a Vault server that uses
 * self-signed certificates.
 *
 * Please note that this test code uses the BetterCloud Vault client to do authentication and policy
 * management. This client is exclusively used in the test code, not in the module's production code,
 * because of performance constraints in this client.
 */
public class VaultTest extends AbstractEngineTestSet {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /* Change this as appropriate. */
    private static final String VAULT_ADDRESS = "http://localhost:8200";
    private static final String ROOT_TOKEN = "671af6ab-5edc-5801-5e9e-389b8545911d";

    /* We need a policy with create, read and delete rights, but _without_ update rights, in order
     * to properly do a getOrCreate. */
    private static final String POLICY_ID = "axoniq_gdpr_module";
    private static final String POLICY_HCL = "path \"secret/*\" { " +
            "capabilities = [\"create\", \"read\", \"delete\"] }";

    private static VaultCryptoEngine vaultCryptoEngine;

    static {
        try {
            /* Set up a BetterCloud Vault client. */
            VaultConfig rootVaultConfig = new VaultConfig()
                    .sslConfig(new SslConfig().verify(false).build())
                    .address(VAULT_ADDRESS)
                    .token(ROOT_TOKEN)
                    .build();
            Vault rootVault = new Vault(rootVaultConfig);

            /* Store our create/read/delete policy. */
            rootVault.logical().write("sys/policy/" + POLICY_ID,
                    Collections.singletonMap("policy", POLICY_HCL));

            /* Obtain a token mapped to this policy. */
            AuthResponse authResponse = rootVault.auth().createToken(
                    new Auth.TokenRequest().polices(Collections.singletonList(POLICY_ID)));
            String token = authResponse.getAuthClientToken();

            /* Create the OkHttpClient and the VaultCryptoEngine. */
            OkHttpClient okHttpClient = Utils.getUnsafeOkHttpClient();
            vaultCryptoEngine = new VaultCryptoEngine(okHttpClient, VAULT_ADDRESS,
                    token,"secret/");
        } catch(Exception ex) {
            logger.error("Unable to configure Vault, Vault tests will be disabled.", ex);
            vaultCryptoEngine = null;
        }
    }

    @Before
    public void beforeMethod() {
        Assume.assumeTrue(vaultCryptoEngine != null);
    }

    protected CryptoEngine getCryptoEngine() {
        return vaultCryptoEngine;
    }

    /**
     * In addition to the standard tests in AbstractEngineTestSet, we want to verify that
     * we don't overwrite keys; this depends on having the right policy configured.
     */
    @Test
    public void updatesDontHappen() throws IOException {
        UUID id = UUID.randomUUID();
        vaultCryptoEngine.putKey(id.toString(), generateKey());
        try {
            vaultCryptoEngine.putKey(id.toString(), generateKey());
            Assert.fail("Key got updated; should have received 403 exception instead.");
        } catch(PermissionDeniedException ex) {
        }
    }

    private SecretKeySpec generateKey() {
        byte[] keyBytes = new byte[32];
        ThreadLocalRandom.current().nextBytes(keyBytes);
        return new SecretKeySpec(keyBytes, "AES");
    }
}
