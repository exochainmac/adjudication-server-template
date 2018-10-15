/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.FieldEncrypter;
import io.axoniq.gdpr.api.PersonalData;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import io.axoniq.gdpr.cryptoengine.InMemoryCryptoEngine;
import lombok.Data;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static io.axoniq.gdpr.utils.TestUtils.isClear;
import static io.axoniq.gdpr.utils.TestUtils.isEncrypted;
import static org.junit.Assert.assertTrue;

/**
 * Tests of encryption of collection elements containing personal data (without wrapper objects).
 */
public class PersDataCollectionsTest {

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
    public void personalDataCollFieldsMustGetEncrypted() {
        A a = new A(3, new ArrayList<>(Arrays.asList(dataFactory.getLastName(), dataFactory.getLastName(), dataFactory.getLastName())));
        logger.info("Before encryption: {}", a);
        fieldEncrypter.encrypt(a);
        logger.info("After encryption: {}", a);
        a.names.forEach(name -> assertTrue(isEncrypted(name)));
        fieldEncrypter.decrypt(a);
        a.names.forEach(name -> assertTrue(isClear(name)));
    }

    @Data public static class A {
        @DataSubjectId private final int id;
        @PersonalData private final List<String> names;
    }


}
