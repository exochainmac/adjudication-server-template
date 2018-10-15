/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.ConfigurationException;
import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.FieldEncrypter;
import io.axoniq.gdpr.api.PersonalData;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import io.axoniq.gdpr.cryptoengine.InMemoryCryptoEngine;
import io.axoniq.gdpr.utils.TestUtils;
import lombok.Data;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Tests of the scenario that a keyid is preloaded through the FieldEncrypter methods rather than provided through
 * a @DataSubjectId
 */
public class PreloadKeyTest {

    private CryptoEngine cryptoEngine;
    private FieldEncrypter fieldEncrypter;

    @Before
    public void setUp() throws Exception {
        cryptoEngine = new InMemoryCryptoEngine();
        fieldEncrypter = new FieldEncrypter(cryptoEngine);
    }

    @Test(expected = ConfigurationException.class)
    public void expectedExceptionTestEncrypt() {
        fieldEncrypter.encrypt(new Person("Frans"));
    }

    @Test(expected = ConfigurationException.class)
    public void expectedExceptionTestDecrypt() {
        fieldEncrypter.decrypt(new Person("Frans"));
    }

    @Test
    public void singleExternalKeyTest() {
        Person person = new Person("Frans");
        String keyId = "bla";
        fieldEncrypter.encrypt(person, keyId);
        Assert.assertTrue(TestUtils.isEncrypted(person.getName()));
        fieldEncrypter.decrypt(person, keyId);
        Assert.assertEquals("Frans", person.getName());
    }

    @Test
    public void multiExternalKeyTest() {
        Person2 person = new Person2(UUID.randomUUID(),"Frans", "kipsaté", "cat");
        String foodKey = "12345";
        String petKey = "67890";
        Map<String, String> keyIds = new HashMap<>();
        keyIds.put("food", foodKey);
        keyIds.put("pet", petKey);

        fieldEncrypter.encrypt(person, keyIds);
        Assert.assertTrue(TestUtils.isEncrypted(person.getName()));
        Assert.assertTrue(TestUtils.isEncrypted(person.getFavouriteFood()));
        Assert.assertTrue(TestUtils.isEncrypted(person.getFavouritePet()));

        cryptoEngine.deleteKey(foodKey);

        fieldEncrypter.decrypt(person, keyIds);
        Assert.assertEquals("Frans", person.getName());
        Assert.assertEquals("", person.getFavouriteFood());
        Assert.assertEquals("cat", person.getFavouritePet());
    }

    @Data public static class Person {
        @PersonalData
        private final String name;
    }

    @Data public static class Person2 {
        @DataSubjectId
        private final UUID id;

        @PersonalData
        private final String name;

        @PersonalData(group = "food")
        private final String favouriteFood;

        @PersonalData(group = "pet")
        private final String favouritePet;
    }
}
