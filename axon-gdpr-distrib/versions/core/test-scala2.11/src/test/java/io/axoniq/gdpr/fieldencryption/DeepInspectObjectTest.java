/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.DeepPersonalData;
import io.axoniq.gdpr.api.FieldEncrypter;
import io.axoniq.gdpr.api.PersonalData;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import io.axoniq.gdpr.cryptoengine.InMemoryCryptoEngine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ThreadLocalRandom;

import static io.axoniq.gdpr.utils.TestUtils.isClear;
import static io.axoniq.gdpr.utils.TestUtils.isEncrypted;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *  Contains tests of encryption in object hierarchies.
 */
public class DeepInspectObjectTest {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CryptoEngine cryptoEngine;
    private FieldEncrypter fieldEncrypter;
    private DataFactory dataFactory;

    @Before
    public void setUp() throws Exception {
        cryptoEngine = new InMemoryCryptoEngine();
        fieldEncrypter = new FieldEncrypter(cryptoEngine);
        fieldEncrypter.setModifyImmutableCollections(false);
        dataFactory = new DataFactory();
        dataFactory.randomize(ThreadLocalRandom.current().nextInt());
    }

    @Test
    public void deepInspectionIsBasedOnAnnotation() {
        A a = new A(1L, new B(dataFactory.getLastName(), null), new B(dataFactory.getLastName(), null));
        logger.info("before encryption {}", a);
        fieldEncrypter.encrypt(a);
        logger.info("after encryption {}", a);
        assertTrue(isEncrypted(a.b1.x));
        assertTrue(isClear(a.b2.x));
        fieldEncrypter.decrypt(a);
        assertTrue(isClear(a.b1.x));
        assertTrue(isClear(a.b2.x));
    }

    @Test
    public void deepInspectionMustWorkRecursively() {
        A a = new A(1L, new B(dataFactory.getLastName(),new C(1L, dataFactory.getFirstName())), null);
        logger.info("before encryption {}", a);
        fieldEncrypter.encrypt(a);
        logger.info("after encryption {}", a);
        assertTrue(isEncrypted(a.b1.x));
        assertTrue(isEncrypted(a.b1.c.x));
        fieldEncrypter.decrypt(a);
        assertTrue(isClear(a.b1.x));
        assertTrue(isClear(a.b1.c.x));
    }

    @Test
    public void multipleKeysMustBeSupported() {
        String lastname = dataFactory.getLastName();
        String firstname = dataFactory.getLastName();
        A a = new A(1L, new B(lastname, new C(2L, firstname)), null);
        fieldEncrypter.encrypt(a);
        cryptoEngine.deleteKey("1");
        fieldEncrypter.decrypt(a);
        logger.info("after encryption, key deletion, decryption {}", a);
        assertEquals("", a.b1.x);
        assertEquals(firstname, a.b1.c.x);
    }

    @Test
    public void cyclesShouldBeUnproblematic() {
        X x1 = new X(1L, dataFactory.getLastName());
        X x2 = new X(2L, dataFactory.getLastName(), x1);
        X x3 = new X(3L, dataFactory.getLastName(), x2);
        x1.x = x3;
        fieldEncrypter.encrypt(x1);
        assertTrue(isEncrypted(x1.name));
        assertTrue(isEncrypted(x2.name));
        assertTrue(isEncrypted(x3.name));
        fieldEncrypter.decrypt(x2);
        assertTrue(isClear(x1.name));
        assertTrue(isClear(x2.name));
        assertTrue(isClear(x3.name));
    }

    @Data public static class A {
        @DataSubjectId private final long id;
        @DeepPersonalData private final B b1;
        private final B b2;
    }

    @Data public static class B {
        @PersonalData private final String x;
        @DeepPersonalData private final C c;
    }

    @Data public static class C {
        @DataSubjectId private final long id;
        @PersonalData private final String x;
    }

    @Data @AllArgsConstructor @RequiredArgsConstructor public static class X {
        @DataSubjectId private final long id;
        @PersonalData private final String name;
        @DeepPersonalData private X x;
    }
}
