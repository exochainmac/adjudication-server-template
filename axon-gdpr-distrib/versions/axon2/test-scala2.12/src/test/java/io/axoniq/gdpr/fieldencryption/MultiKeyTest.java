/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.*;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import io.axoniq.gdpr.cryptoengine.InMemoryCryptoEngine;
import io.axoniq.gdpr.utils.TestUtils;
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

public class MultiKeyTest {

    private CryptoEngine cryptoEngine;
    private FieldEncrypter fieldEncrypter;

    @Data
    public static class NewCustomerEvent {
        @DataSubjectId(group = "name", prefix = "name-")
        @DataSubjectId(group = "address", prefix = "address-")
        private final int id;

        @PersonalData(group = "name")
        private final String name;

        @PersonalData(group = "address")
        private final String address;
    }

    @Before
    public void setUp() throws Exception {
        cryptoEngine = new InMemoryCryptoEngine();
        fieldEncrypter = new FieldEncrypter(cryptoEngine);
    }

    @Test
    public void newCustomerEventTest() {
        NewCustomerEvent event = new NewCustomerEvent(132, "testName", "testAddress");
        fieldEncrypter.encrypt(event);
        Assert.assertTrue(TestUtils.isEncrypted(event.name));
        Assert.assertTrue(TestUtils.isEncrypted(event.address));
        fieldEncrypter.decrypt(event);
        Assert.assertEquals("testName", event.name);
        Assert.assertEquals("testAddress", event.address);
        fieldEncrypter.encrypt(event);
        cryptoEngine.deleteKey("name-132");
        fieldEncrypter.decrypt(event);
        Assert.assertEquals("", event.name);
        Assert.assertEquals("testAddress", event.address);
    }
}
