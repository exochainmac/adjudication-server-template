/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.FieldEncrypter;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import io.axoniq.gdpr.cryptoengine.InMemoryCryptoEngine;
import io.axoniq.gdpr.utils.TestUtils;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Contains a few elementary tests of the FieldEncrypter, encrypting byte[] and String fields in a single class.
 * This is implemented an an abstract test class, allowing us to run these tests against vanilla Java, Lombok
 * and Kotlin classes.
 */
public abstract class AbstractBasicTestSet<T> {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CryptoEngine cryptoEngine;
    private FieldEncrypter fieldEncrypter;
    private DataFactory dataFactory;

    public abstract T createEvent(UUID id, String name, byte[] picture, String city);
    public abstract UUID getId(T event);
    public abstract String getName(T event);
    public abstract byte[] getPicture(T event);
    public abstract String getCity(T event);

    @Before
    public void setUp() throws Exception {
        cryptoEngine = new InMemoryCryptoEngine();
        fieldEncrypter = new FieldEncrypter(cryptoEngine);
        dataFactory = new DataFactory();
        dataFactory.randomize(ThreadLocalRandom.current().nextInt());
    }

    @Test
    public void personalDataFieldsMustGetEncrypted() {
        UUID idOriginal = UUID.randomUUID();
        String nameOriginal = dataFactory.getLastName();
        byte[] pictureOriginal = dataFactory.getRandomChars(20).getBytes(StandardCharsets.UTF_8);
        String cityOriginal = dataFactory.getCity();

        T personRegisteredEvent = createEvent(idOriginal, nameOriginal, pictureOriginal.clone(), cityOriginal);
        logger.info("Before encryption: {}", personRegisteredEvent);
        fieldEncrypter.encrypt(personRegisteredEvent);
        logger.info("After encryption: {}", personRegisteredEvent);

        Assert.assertEquals(idOriginal, getId(personRegisteredEvent));
        Assert.assertNotEquals(nameOriginal, getName(personRegisteredEvent));
        Assert.assertNotEquals(pictureOriginal, getPicture(personRegisteredEvent));
        Assert.assertEquals(cityOriginal, getCity(personRegisteredEvent));

        Assert.assertTrue(TestUtils.isEncrypted(getName(personRegisteredEvent)));
        Assert.assertTrue(TestUtils.isEncrypted(getPicture(personRegisteredEvent)));
        Assert.assertTrue(!TestUtils.isEncrypted(getCity(personRegisteredEvent)));
    }

    @Test
    public void decryptionMustRestoreTheOriginal() {
        UUID idOriginal = UUID.randomUUID();
        String nameOriginal = dataFactory.getLastName();
        byte[] pictureOriginal = dataFactory.getRandomChars(20).getBytes(StandardCharsets.UTF_8);
        String cityOriginal = dataFactory.getCity();

        T personRegisteredEvent1 = createEvent(idOriginal, nameOriginal, pictureOriginal.clone(), cityOriginal);
        T personRegisteredEvent2 = createEvent(idOriginal, nameOriginal, pictureOriginal.clone(), cityOriginal);
        Assert.assertEquals(personRegisteredEvent1, personRegisteredEvent2);

        fieldEncrypter.encrypt(personRegisteredEvent2);
        Assert.assertNotEquals(personRegisteredEvent1, personRegisteredEvent2);

        fieldEncrypter.decrypt(personRegisteredEvent2);
        Assert.assertEquals(personRegisteredEvent1, personRegisteredEvent2);
    }

    @Test
    public void keyDeletionMustPreventDecryption() {
        UUID idOriginal = UUID.randomUUID();
        String nameOriginal = dataFactory.getLastName();
        byte[] pictureOriginal = dataFactory.getRandomChars(20).getBytes(StandardCharsets.UTF_8);
        String cityOriginal = dataFactory.getCity();

        T personRegisteredEvent = createEvent(idOriginal, nameOriginal, pictureOriginal.clone(), cityOriginal);
        logger.info("Before encryption: {}", personRegisteredEvent);
        fieldEncrypter.encrypt(personRegisteredEvent);
        logger.info("After encryption: {}", personRegisteredEvent);
        cryptoEngine.deleteKey(idOriginal.toString());
        fieldEncrypter.decrypt(personRegisteredEvent);
        logger.info("After decryption with deleted key: {}", personRegisteredEvent);

        Assert.assertEquals(idOriginal, getId(personRegisteredEvent));
        Assert.assertEquals("", getName(personRegisteredEvent));
        Assert.assertEquals(null, getPicture(personRegisteredEvent));
        Assert.assertEquals(cityOriginal, getCity(personRegisteredEvent));
    }

    @Test
    public void replaceMustDeleteValue() {
        UUID idOriginal = UUID.randomUUID();
        String nameOriginal = dataFactory.getLastName();
        byte[] pictureOriginal = dataFactory.getRandomChars(20).getBytes(StandardCharsets.UTF_8);
        String cityOriginal = dataFactory.getCity();

        T personRegisteredEvent = createEvent(idOriginal, nameOriginal, pictureOriginal.clone(), cityOriginal);
        logger.info("Before replacement: {}", personRegisteredEvent);
        fieldEncrypter.replace(personRegisteredEvent);
        logger.info("After replacement: {}", personRegisteredEvent);

        Assert.assertEquals(idOriginal, getId(personRegisteredEvent));
        Assert.assertEquals("", getName(personRegisteredEvent));
        Assert.assertEquals(null, getPicture(personRegisteredEvent));
        Assert.assertEquals(cityOriginal, getCity(personRegisteredEvent));
    }


    @Test
    public void encryptionMustHaveRandomness() {
        UUID idOriginal = UUID.randomUUID();
        String nameOriginal = dataFactory.getLastName();
        byte[] pictureOriginal = dataFactory.getRandomChars(20).getBytes(StandardCharsets.UTF_8);
        String cityOriginal = dataFactory.getCity();

        T personRegisteredEvent1 = createEvent(idOriginal, nameOriginal, pictureOriginal.clone(), cityOriginal);
        T personRegisteredEvent2 = createEvent(idOriginal, nameOriginal, pictureOriginal.clone(), cityOriginal);
        Assert.assertEquals(personRegisteredEvent1, personRegisteredEvent2);

        fieldEncrypter.encrypt(personRegisteredEvent1);
        fieldEncrypter.encrypt(personRegisteredEvent2);
        Assert.assertNotEquals(personRegisteredEvent1, personRegisteredEvent2);
    }
    
    
}
