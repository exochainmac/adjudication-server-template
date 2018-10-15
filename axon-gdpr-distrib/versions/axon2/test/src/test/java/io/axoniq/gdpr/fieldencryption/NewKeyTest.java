/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.*;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import io.axoniq.gdpr.cryptoengine.InMemoryCryptoEngine;
import lombok.Data;
import lombok.Value;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Tests of the scenario that a key has changed.
 */
public class NewKeyTest {

    private CryptoEngine cryptoEngine;
    private FieldEncrypter fieldEncrypter;

    @Before
    public void setUp() throws Exception {
        cryptoEngine = new InMemoryCryptoEngine();
        fieldEncrypter = new FieldEncrypter(cryptoEngine);
    }

    @Test
    public void smokeTest() {
        Person p = new Person("x", "Frans");
        fieldEncrypter.encrypt(p);
        fieldEncrypter.decrypt(p);
        Assert.assertEquals("Frans", p.getName());
        fieldEncrypter.encrypt(p);
        cryptoEngine.deleteKey("x");
        cryptoEngine.getOrCreateKey("x");
        fieldEncrypter.decrypt(p);
        Assert.assertEquals("", p.getName());
    }

    @Data public static class Person {
        @DataSubjectId
        private final String id;
        @PersonalData
        private final String name;
    }
}
