/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.*;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import io.axoniq.gdpr.cryptoengine.InMemoryCryptoEngine;
import io.axoniq.gdpr.utils.TestUtils;
import lombok.Data;
import io.axoniq.gdpr.serialization.SerializedObject;
import io.axoniq.gdpr.serialization.xml.XStreamSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Tests of the scenario that we do double (non-idempotent) encryption.
 */
public class DoubleEncryptionTest {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @PersonalData(group = "", reencrypt = true)    /* reencrypt=true switches off idempotency checkings */
    @PersonalData(group = "sensitive") /* We'll keep idempotency for the first encryption layer, 'sensitive' */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface SensitivePersonalData {
    }

    @Data static class VO {
        @SensitivePersonalData private final String superSecret;
        @PersonalData private final String regularData;
        private final String clearData;
    }

    @Data static class DTO {
        @DeepPersonalData private final VO value;
    }

    @Data static class Event {
        @DataSubjectId private final UUID id;
        @DeepPersonalData private final VO value;
    }

    @Test
    public void integratedTest() throws Exception {
        /* Setting up the infra. */
            /* A. Shared crypto engine. */
        CryptoEngine cryptoEngine = new InMemoryCryptoEngine();
            /* B. Entry point encrypter. */
        FieldEncrypter entryPointFieldEncrypter = new FieldEncrypter(cryptoEngine);
            /* C. Event field encrypter + serializer. */
        FieldEncrypter eventFieldEncrypter = new FieldEncrypter(cryptoEngine);
        eventFieldEncrypter.setGroups(Collections.singleton("")); /* restricting to default group */
        FieldEncryptingSerializer fieldEncryptingSerializer = new FieldEncryptingSerializer(eventFieldEncrypter, new XStreamSerializer());
        UUID customerId = UUID.randomUUID();

        /* Phase 1: Encryption of super secret data */
        DTO dto = new DTO(new VO("something_super_secret", "something_private", "something_public"));
        entryPointFieldEncrypter.encrypt(dto,
                Collections.singletonMap("sensitive", customerId.toString()), /* pre-loaded keyId for group "sensitive" */
                Collections.singleton("sensitive"));  /* last argument: set of groups to process */
        Assert.assertTrue(TestUtils.isEncrypted(dto.value.superSecret));
        Assert.assertFalse(TestUtils.isEncrypted(dto.value.regularData));
        Assert.assertFalse(TestUtils.isEncrypted(dto.value.clearData));

        /* Phase 2: Encryption with serialization */
        Event event = new Event(customerId, dto.value);
        SerializedObject<String> serializedEvent = fieldEncryptingSerializer.serialize(event, String.class);
        logger.info(serializedEvent.getData());
        Assert.assertFalse(serializedEvent.getData().contains("something_super_secret"));
        Assert.assertFalse(serializedEvent.getData().contains("something_private"));
        Assert.assertTrue(serializedEvent.getData().contains("something_public"));

        /* Phase 3: Decryption with deserialization */
        Event event2 = fieldEncryptingSerializer.deserialize(serializedEvent);
        Assert.assertEquals("something_public", event2.value.clearData);
        Assert.assertEquals("something_private", event2.value.regularData);
        Assert.assertTrue(TestUtils.isEncrypted(event2.value.superSecret));

        /* Phase 4: Decryption of super secret data */
        entryPointFieldEncrypter.decrypt(event2.value,
                Collections.singletonMap("sensitive", event2.getId().toString()), /* pre-loaded keyId for group "sensitive" */
                Collections.singleton("sensitive"));  /* last argument: set of groups to process */
        Assert.assertEquals("something_public", event2.value.clearData);
        Assert.assertEquals("something_private", event2.value.regularData);
        Assert.assertEquals("something_super_secret", event2.value.superSecret);
    }

    @Test
    public void orderTest1() throws Exception {
        FieldEncrypter fieldEncrypter = new FieldEncrypter(new InMemoryCryptoEngine());
        Map<String, String> keyIds = new HashMap<>();
        keyIds.put("", "1");
        keyIds.put("sensitive", "2");
        VO vo = new VO("something_super_secret", "something_private", "something_public");

        fieldEncrypter.encrypt(vo, keyIds);
        Assert.assertEquals("something_public", vo.clearData);
        Assert.assertTrue(TestUtils.isEncrypted(vo.regularData));
        Assert.assertTrue(TestUtils.isEncrypted(vo.superSecret));

        fieldEncrypter.decrypt(vo, Collections.singletonMap("", "1"), Collections.singleton(""));
        Assert.assertEquals("something_public", vo.clearData);
        Assert.assertEquals("something_private", vo.regularData);
        Assert.assertTrue(TestUtils.isEncrypted(vo.superSecret));

        fieldEncrypter.decrypt(vo, Collections.singletonMap("sensitive", "2"), Collections.singleton("sensitive"));
        Assert.assertEquals("something_public", vo.clearData);
        Assert.assertEquals("something_private", vo.regularData);
        Assert.assertEquals("something_super_secret", vo.superSecret);
    }

    @Test
    public void orderTest2() throws Exception {
        FieldEncrypter fieldEncrypter = new FieldEncrypter(new InMemoryCryptoEngine());
        Map<String, String> keyIds = new HashMap<>();
        keyIds.put("", "1");
        keyIds.put("sensitive", "2");
        VO vo = new VO("something_super_secret", "something_private", "something_public");

        fieldEncrypter.encrypt(vo, keyIds);
        Assert.assertEquals("something_public", vo.clearData);
        Assert.assertTrue(TestUtils.isEncrypted(vo.regularData));
        Assert.assertTrue(TestUtils.isEncrypted(vo.superSecret));

        fieldEncrypter.decrypt(vo, keyIds);
        Assert.assertEquals("something_public", vo.clearData);
        Assert.assertEquals("something_private", vo.regularData);
        Assert.assertEquals("something_super_secret", vo.superSecret);
    }

    @Data static class A {
        @DataSubjectId(group = "1") @DataSubjectId(group = "2") private final UUID id;
        @SerializedPersonalData(group = "1") private final String x;
        @PersonalData(group = "2", reencrypt = true) private final String xEncrypted;
    }

    @Test
    public void serializedPlusEncryptedTest1() throws Exception {
        FieldEncrypter fieldEncrypter = new FieldEncrypter(new InMemoryCryptoEngine());
        A a = new A(UUID.randomUUID(), "test", null);
        fieldEncrypter.encrypt(a);
        Assert.assertNull(a.x);
        Assert.assertTrue(TestUtils.isEncrypted(a.xEncrypted));
        fieldEncrypter.decrypt(a, Collections.singleton("2"));
        Assert.assertNull(a.x);
        Assert.assertTrue(TestUtils.isSerializedEncrypted(a.xEncrypted));
        fieldEncrypter.decrypt(a, Collections.singleton("1"));
        Assert.assertEquals("test", a.x);
        Assert.assertNull(a.xEncrypted);
    }

    @Test
    public void serializedPlusEncryptedTest2() throws Exception {
        FieldEncrypter fieldEncrypter = new FieldEncrypter(new InMemoryCryptoEngine());
        A a = new A(UUID.randomUUID(), "test", null);
        fieldEncrypter.encrypt(a);
        Assert.assertNull(a.x);
        Assert.assertTrue(TestUtils.isEncrypted(a.xEncrypted));
        fieldEncrypter.decrypt(a);
        Assert.assertEquals("test", a.x);
        Assert.assertNull(a.xEncrypted);
    }

    @Data static class B {
        @DataSubjectId(group = "1") @DataSubjectId(group = "2") private final UUID id;
        @PersonalData(group = "2", scope = Scope.VALUE, reencrypt = true)
        @PersonalData(group = "1", scope = Scope.VALUE)
        private final Map<String, String> data;
    }

    @Test
    public void doubleEncryptedMapTest1() throws Exception {
        FieldEncrypter fieldEncrypter = new FieldEncrypter(new InMemoryCryptoEngine());
        B b = new B(UUID.randomUUID(), Collections.singletonMap("testKey", "testValue"));
        fieldEncrypter.encrypt(b);
        Assert.assertTrue(TestUtils.isEncrypted(b.data.get("testKey")));
        fieldEncrypter.decrypt(b, Collections.singleton("2"));
        Assert.assertTrue(TestUtils.isEncrypted(b.data.get("testKey")));
        fieldEncrypter.decrypt(b, Collections.singleton("1"));
        Assert.assertEquals("testValue", b.data.get("testKey"));
    }

    @Test
    public void doubleEncryptedMapTest2() throws Exception {
        FieldEncrypter fieldEncrypter = new FieldEncrypter(new InMemoryCryptoEngine());
        B b = new B(UUID.randomUUID(), Collections.singletonMap("testKey", "testValue"));
        fieldEncrypter.encrypt(b);
        Assert.assertTrue(TestUtils.isEncrypted(b.data.get("testKey")));
        fieldEncrypter.decrypt(b);
        Assert.assertEquals("testValue", b.data.get("testKey"));
    }

}
