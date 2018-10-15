/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.cryptoengine;

import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.*;

public abstract class AbstractEngineTestSet {

    private CryptoEngine cryptoEngine;

    @Before
    public void init() {
        cryptoEngine = getCryptoEngine();
    }

    protected abstract CryptoEngine getCryptoEngine();

    @Test
    public void repeatedInvocationsWithSameIdGiveSameResult() {
        String id1 = UUID.randomUUID().toString();
        SecretKey key1 = cryptoEngine.getOrCreateKey(id1);
        SecretKey key2 = cryptoEngine.getOrCreateKey(id1);
        assertTrue(keysEquivalent(key1, key2));
    }

    @Test
    public void repeatedInvocationsWithDifferentIdGiveDifferentResult() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        SecretKey key1 = cryptoEngine.getOrCreateKey(id1);
        SecretKey key2 = cryptoEngine.getOrCreateKey(id2);
        assertFalse(keysEquivalent(key1, key2));
    }

    @Test
    public void getDoesNotCreateKeys() {
        String id1 = UUID.randomUUID().toString();
        SecretKey key1 = cryptoEngine.getKey(id1);
        assertNull(key1);
    }

    @Test
    public void keyCanBeDeleted() {
        String id1 = UUID.randomUUID().toString();
        SecretKey key1 = cryptoEngine.getOrCreateKey(id1);
        assertNotNull(key1);
        cryptoEngine.deleteKey(id1);
        SecretKey key2 = cryptoEngine.getKey(id1);
        assertNull(key2);
    }

    boolean keysEquivalent(SecretKey key1, SecretKey key2) {
        try {
            byte[] clearBytes = "This is a test".getBytes(StandardCharsets.UTF_8);
            byte[] iv = new byte[16];
            Cipher encCipher = cryptoEngine.createCipher();
            encCipher.init(Cipher.ENCRYPT_MODE, key1, new IvParameterSpec(iv));
            byte[] cryptoBytes = encCipher.doFinal(clearBytes);
            Cipher decCipher = cryptoEngine.createCipher();
            decCipher.init(Cipher.DECRYPT_MODE, key2, new IvParameterSpec(iv));
            try {
                byte[] decryptedBytes = decCipher.doFinal(cryptoBytes);
                return Arrays.equals(clearBytes, decryptedBytes);
            } catch(Exception ex) {
                return false;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
