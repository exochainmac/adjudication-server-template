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
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 *  Contains tests of encryption in objects in special collections (immutables etc.)
 */
public class SpecialCollectionsTest {

    private CryptoEngine cryptoEngine;
    private FieldEncrypter fieldEncrypter;
    private Serializer serializer = new XStreamSerializer();
    private DataFactory dataFactory;

    @Before
    public void setUp() throws Exception {
        cryptoEngine = new InMemoryCryptoEngine();
        fieldEncrypter = new FieldEncrypter(cryptoEngine);
        dataFactory = new DataFactory();
        dataFactory.randomize(ThreadLocalRandom.current().nextInt());
    }

    @Test
    public void nullMustStayNull() {
        A a = new A(1L, null);
        fieldEncrypter.encrypt(a);
        Assert.assertNull(a.data);
        fieldEncrypter.decrypt(a);
        Assert.assertNull(a.data);
    }

    @Test
    public void emptyList() {
        A a = new A(1L, Collections.EMPTY_LIST);
        fieldEncrypter.encrypt(a);
        Assert.assertTrue(a.data.isEmpty());
        fieldEncrypter.decrypt(a);
        Assert.assertTrue(a.data.isEmpty());
    }

    @Test
    public void singletonList() {
        A a = new A(1L, Collections.singletonList("TEST"));
        fieldEncrypter.encrypt(a);
        Assert.assertFalse(a.data.get(0).equals("TEST"));
        fieldEncrypter.decrypt(a);
        Assert.assertTrue(a.data.get(0).equals("TEST"));
    }

    @Test
    public void singletonSet() {
        B a = new B(1L, Collections.singleton("TEST"));
        fieldEncrypter.encrypt(a);
        Assert.assertFalse(a.data.contains("TEST"));
        fieldEncrypter.decrypt(a);
        Assert.assertTrue(a.data.contains("TEST"));
    }

    @Test
    public void unmodifiableList() {
        List<String> list = new ArrayList<>();
        list.add("TEST1");
        list.add("TEST2");
        A a = new A(1L, Collections.unmodifiableList(list));
        String serialized1 = serializer.serialize(a, String.class).getData();
        System.out.println(serialized1);
        Assert.assertTrue(serialized1.contains("TEST"));
        fieldEncrypter.encrypt(a);
        String serialized2 = serializer.serialize(a, String.class).getData();
        System.out.println(serialized2);
        Assert.assertFalse(serialized2.contains("TEST"));
        fieldEncrypter.decrypt(a);
        String serialized3 = serializer.serialize(a, String.class).getData();
        Assert.assertEquals(serialized1, serialized3);
    }

    @Test
    public void arrayAsList() {
        A a = new A(1L, Arrays.asList("TEST1", "TEST2"));
        String serialized1 = serializer.serialize(a, String.class).getData();
        System.out.println(serialized1);
        Assert.assertTrue(serialized1.contains("TEST"));
        fieldEncrypter.encrypt(a);
        String serialized2 = serializer.serialize(a, String.class).getData();
        System.out.println(serialized2);
        Assert.assertFalse(serialized2.contains("TEST"));
        fieldEncrypter.decrypt(a);
        String serialized3 = serializer.serialize(a, String.class).getData();
        Assert.assertEquals(serialized1, serialized3);
    }

    @Test
    public void synchronizedUnmodifiableList() {
        List<String> list = new ArrayList<>();
        list.add("TEST1");
        list.add("TEST2");
        A a = new A(1L, Collections.synchronizedList(Collections.unmodifiableList(list)));
        String serialized1 = serializer.serialize(a, String.class).getData();
        System.out.println(serialized1);
        Assert.assertTrue(serialized1.contains("TEST"));
        fieldEncrypter.encrypt(a);
        String serialized2 = serializer.serialize(a, String.class).getData();
        System.out.println(serialized2);
        Assert.assertFalse(serialized2.contains("TEST"));
        fieldEncrypter.decrypt(a);
        String serialized3 = serializer.serialize(a, String.class).getData();
        Assert.assertEquals(serialized1, serialized3);
    }

    @Test
    public void checkedUnmodifiableSortedSet() {
        SortedSet<String> set = new TreeSet<>();
        set.add("TEST1");
        set.add("TEST2");
        B a = new B(1L, Collections.checkedSortedSet(Collections.unmodifiableSortedSet(set), String.class));
        String serialized1 = serializer.serialize(a, String.class).getData();
        System.out.println(serialized1);
        Assert.assertTrue(serialized1.contains("TEST"));
        fieldEncrypter.encrypt(a);
        String serialized2 = serializer.serialize(a, String.class).getData();
        System.out.println(serialized2);
        Assert.assertFalse(serialized2.contains("TEST"));
        fieldEncrypter.decrypt(a);
        String serialized3 = serializer.serialize(a, String.class).getData();
        Assert.assertEquals(serialized1, serialized3);
    }

    @Data public static class A {
        @DataSubjectId private final long id;
        @PersonalData private final List<String> data;
    }

    @Data public static class B {
        @DataSubjectId private final long id;
        @PersonalData private final Set<String> data;
    }
}
