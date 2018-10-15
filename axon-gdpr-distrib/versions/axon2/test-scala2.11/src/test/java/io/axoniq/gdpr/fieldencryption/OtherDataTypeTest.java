/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.FieldEncrypter;
import io.axoniq.gdpr.api.PersonalData;
import io.axoniq.gdpr.api.SerializedPersonalData;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import io.axoniq.gdpr.cryptoengine.InMemoryCryptoEngine;
import lombok.Data;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

public class OtherDataTypeTest {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CryptoEngine cryptoEngine;
    private FieldEncrypter fieldEncrypter;
    private DataFactory dataFactory;

    @Before
    public void setUp() throws Exception {
        cryptoEngine = new InMemoryCryptoEngine();
        fieldEncrypter = new FieldEncrypter(cryptoEngine);
        dataFactory = new DataFactory();
        dataFactory.randomize(ThreadLocalRandom.current().nextInt());
    }

    @Test
    public void otherDataTypesMustGetEncryptedInSeparateField() {
        LocalDate date = LocalDate.now();
        PersonEvent evt = new PersonEvent(1, date, 3);
        logger.info("before encryption: {}", evt);
        assertNull(evt.dateOfBirthEncrypted);
        assertNull(evt.nEncrypted);
        fieldEncrypter.encrypt(evt);
        logger.info("after encryption: {}", evt);
        assertNull(evt.dateOfBirth);
        assertNotNull(evt.dateOfBirthEncrypted);
        assertEquals(0, evt.n);
        assertNotNull(evt.nEncrypted);
    }

    @Test
    public void otherDataTypeMustBeReplaceable() {
        LocalDate date = LocalDate.now();
        int n = 3;
        PersonEvent evt = new PersonEvent(1, date, n);
        fieldEncrypter.encrypt(evt);
        fieldEncrypter.decrypt(evt);
        assertEquals(date, evt.dateOfBirth);
        assertNull(evt.dateOfBirthEncrypted);
        assertEquals((long)n, (long)evt.n);
        assertNull(evt.nEncrypted);
    }


    @Test
    public void nullMustBeHandledCorrectly() {
        int n = 3;
        PersonEvent evt = new PersonEvent(1, null, n);
        fieldEncrypter.encrypt(evt);
        assertNull(evt.dateOfBirth);
        assertNull(evt.dateOfBirthEncrypted);
        fieldEncrypter.decrypt(evt);
        assertNull(evt.dateOfBirth);
        assertNull(evt.dateOfBirthEncrypted);
    }

    @Data
    public static class PersonEvent {
        @DataSubjectId
        private final int id;

        @SerializedPersonalData
        private final LocalDate dateOfBirth;
        private byte[] dateOfBirthEncrypted;

        @SerializedPersonalData
        private final int n;
        private String nEncrypted;

    }

}
