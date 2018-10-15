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
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static io.axoniq.gdpr.utils.TestUtils.isClear;
import static io.axoniq.gdpr.utils.TestUtils.isEncrypted;
import static org.junit.Assert.assertTrue;

/**
 * Tests of encryption of maps.
 */
public class MapTest {

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
    public void mapSupportSmokeTest() {
        HashMap<String, String> emails = new HashMap<>();
        emails.put("private", dataFactory.getEmailAddress());
        emails.put("work", dataFactory.getEmailAddress());
        Person person = new Person(1, emails);
        logger.info("Person before encryption: {}", person);
        Assert.assertTrue(person.emailAddresses.containsKey("private"));
        Assert.assertTrue(person.emailAddresses.containsKey("work"));
        Assert.assertTrue(person.emailAddresses.get("private").contains("@"));
        Assert.assertTrue(person.emailAddresses.get("work").contains("@"));
        fieldEncrypter.encrypt(person);
        Assert.assertTrue(person.emailAddresses.containsKey("private"));
        Assert.assertTrue(person.emailAddresses.containsKey("work"));
        Assert.assertFalse(person.emailAddresses.get("private").contains("@"));
        Assert.assertFalse(person.emailAddresses.get("work").contains("@"));
        logger.info("Person after encryption: {}", person);
        fieldEncrypter.decrypt(person);
        Assert.assertTrue(person.emailAddresses.containsKey("private"));
        Assert.assertTrue(person.emailAddresses.containsKey("work"));
        Assert.assertTrue(person.emailAddresses.get("private").contains("@"));
        Assert.assertTrue(person.emailAddresses.get("work").contains("@"));
        logger.info("Person after decryption: {}", person);
    }

    @Data public static class Person {
        @DataSubjectId
        private final int id;
        @PersonalData(scope = Scope.VALUE)
        private final Map<String, String> emailAddresses;
    }

    @Test
    public void personalDataInKeyTest() {
        HashMap<String, String> x = new HashMap<>();
        x.put("test", "test");
        A a = new A(1, x);
        fieldEncrypter.encrypt(a);
        Assert.assertFalse(a.x.containsKey("test"));
        Assert.assertTrue(a.x.containsValue("test"));
        fieldEncrypter.decrypt(a);
        Assert.assertEquals("test", a.x.get("test"));
    }

    @Data public static class A {
        @DataSubjectId
        private final int id;
        @PersonalData(scope = Scope.KEY)
        private final Map<String, String> x;
    }

    @Test
    public void personalDataInBothTest() {
        HashMap<String, String> x = new HashMap<>();
        x.put("test", "test");
        B b = new B(1, x);
        fieldEncrypter.encrypt(b);
        Assert.assertFalse(b.x.containsKey("test"));
        Assert.assertFalse(b.x.containsValue("test"));
        fieldEncrypter.decrypt(b);
        Assert.assertEquals("test", b.x.get("test"));
    }

    @Data public static class B {
        @DataSubjectId
        private final int id;
        @PersonalData(scope = Scope.BOTH)
        private final Map<String, String> x;
    }

    @Test
    public void deepPersonalDataInKeyTest() {
        HashMap<PDO, PDO> x = new HashMap<>();
        x.put(new PDO("test"), new PDO("test"));
        C c = new C(1, x);
        fieldEncrypter.encrypt(c);
        Assert.assertFalse(c.x.containsKey(new PDO("test")));
        Assert.assertTrue(c.x.containsValue(new PDO("test")));
        fieldEncrypter.decrypt(c);
        Assert.assertEquals(new PDO("test"), c.x.get(new PDO("test")));
    }

    @Value public static class PDO {
        @PersonalData String x;
    }

    @Data public static class C {
        @DataSubjectId
        private final int id;
        @DeepPersonalData(scope = Scope.KEY)
        private final Map<PDO, PDO> x;
    }

    @Test
    public void deepPersonalDataInValueTest() {
        HashMap<PDO, PDO> x = new HashMap<>();
        x.put(new PDO("test"), new PDO("test"));
        D d = new D(1, x);
        fieldEncrypter.encrypt(d);
        Assert.assertTrue(d.x.containsKey(new PDO("test")));
        Assert.assertFalse(d.x.containsValue(new PDO("test")));
        fieldEncrypter.decrypt(d);
        Assert.assertEquals(new PDO("test"), d.x.get(new PDO("test")));
    }

    @Data public static class D {
        @DataSubjectId
        private final int id;
        @DeepPersonalData(scope = Scope.VALUE)
        private final Map<PDO, PDO> x;
    }

    @Test
    public void deepPersonalDataInBothTest() {
        HashMap<PDO, PDO> x = new HashMap<>();
        x.put(new PDO("test"), new PDO("test"));
        E e = new E(1, x);
        fieldEncrypter.encrypt(e);
        Assert.assertFalse(e.x.containsKey(new PDO("test")));
        Assert.assertFalse(e.x.containsValue(new PDO("test")));
        fieldEncrypter.decrypt(e);
        Assert.assertEquals(new PDO("test"), e.x.get(new PDO("test")));
    }

    @Data public static class E {
        @DataSubjectId
        private final int id;
        @DeepPersonalData(scope = Scope.BOTH)
        private final Map<PDO, PDO> x;
    }

    @Test
    public void mixImmediateAndDeepPersonalDataTest() {
        HashMap<String, PDO> x = new HashMap<>();
        x.put("test", new PDO("test"));
        F f = new F(1, x);
        fieldEncrypter.encrypt(f);
        Assert.assertFalse(f.x.containsKey("test"));
        Assert.assertFalse(f.x.containsValue(new PDO("test")));
        fieldEncrypter.decrypt(f);
        Assert.assertEquals(new PDO("test"), f.x.get("test"));
    }

    @Data public static class F {
        @DataSubjectId
        private final int id;
        @PersonalData(scope = Scope.KEY)
        @DeepPersonalData(scope = Scope.VALUE)
        private final Map<String, PDO> x;
    }

    @Test
    public void subjInKeyWithDeepData() {
        Map<String, PDO> x = new HashMap<>();
        x.put("key1", new PDO("test"));
        x.put("key2", new PDO("test"));
        G g = new G(1, x);
        fieldEncrypter.encrypt(g);
        cryptoEngine.deleteKey("key1");
        fieldEncrypter.decrypt(g);
        Assert.assertEquals("", g.x.get("key1").x);
        Assert.assertEquals("test", g.x.get("key2").x);
    }

    @Data public static class G {
        @DataSubjectId
        private final int id;
        @DataSubjectId(scope = Scope.KEY)
        @DeepPersonalData(scope = Scope.VALUE)
        private final Map<String, PDO> x;
    }

    @Test
    public void subjInKeyWithImmediateData() {
        Map<String, String> x = new HashMap<>();
        x.put("key1", "test");
        x.put("key2", "test");
        H h = new H(1, x);
        fieldEncrypter.encrypt(h);
        cryptoEngine.deleteKey("key2");
        fieldEncrypter.decrypt(h);
        Assert.assertEquals("test", h.x.get("key1"));
        Assert.assertEquals("", h.x.get("key2"));
    }

    @Data public static class H {
        @DataSubjectId
        private final int id;
        @DataSubjectId(scope = Scope.KEY)
        @PersonalData(scope = Scope.VALUE)
        private final Map<String, String> x;
    }

    @Test
    public void ummodifiableMapTest() {
        HashMap<String, String> x = new HashMap<>();
        x.put("test", "test");
        B b = new B(1, Collections.synchronizedMap(Collections.unmodifiableMap(x)));
        fieldEncrypter.encrypt(b);
        Assert.assertFalse(b.x.containsKey("test"));
        Assert.assertFalse(b.x.containsValue("test"));
        fieldEncrypter.decrypt(b);
        Assert.assertEquals("test", b.x.get("test"));
    }

    @Test
    public void emptyMapTest() {
        B b = new B(1, Collections.emptyMap());
        fieldEncrypter.encrypt(b);
        Assert.assertTrue(b.x.isEmpty());
        fieldEncrypter.decrypt(b);
        Assert.assertTrue(b.x.isEmpty());
    }

    @Test
    public void singletonMapTest() {
        B b = new B(1, Collections.singletonMap("test", "test"));
        fieldEncrypter.encrypt(b);
        Assert.assertFalse(b.x.containsKey("test"));
        Assert.assertFalse(b.x.containsValue("test"));
        fieldEncrypter.decrypt(b);
        Assert.assertEquals("test", b.x.get("test"));
    }
}
