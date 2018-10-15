/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.*;
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
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 *  Contains tests of encryption in objects in collections in multi levels
 */
public class MultiLevelRecursionTest {

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
    public void deepInspectionCompletenessTest() {
        A a = randomA();
        String serialized1 = serializer.serialize(a, String.class).getData();
        System.out.println(serialized1);
        Assert.assertTrue(serialized1.contains("@"));
        Assert.assertTrue(serialized1.contains("AAAAAAAAAAAAAA=="));
        Assert.assertTrue(serialized1.contains("2017"));
        fieldEncrypter.encrypt(a);
        String serialized2 = serializer.serialize(a, String.class).getData();
        Assert.assertFalse(serialized2.contains("@"));
        Assert.assertFalse(serialized2.contains("AAAAAAAAAAAAAA=="));
        Assert.assertFalse(serialized2.contains("2017"));
        System.out.println(serialized2);
        fieldEncrypter.decrypt(a);
        String serialized3 = serializer.serialize(a, String.class).getData();
        Assert.assertEquals(serialized1, serialized3);
    }

    @Data public static class A {
        @DataSubjectId private final long id;
        @PersonalData private final List<? extends Set<String[]>> b;
        @PersonalData private final List<byte[]> c;
        @DeepPersonalData private final List<? extends Set<B[]>> d;
        @DeepPersonalData private final HashSet<List<B>>[] f;
    }

    @Data public static class B {
        @PersonalData private final String x;
        @PersonalData private final List<String[]> y;
        @SerializedPersonalData private final LocalDate z;
        private byte[] zEncrypted;
    }

    private A randomA() {
        return new A(1L, randomListOfSetOfStringArray(), fixedListOfByteArray(), randomListOfSetOfBArray(), randomArrayOfSetOfBlist());
    }

    private List<Set<String[]>> randomListOfSetOfStringArray() {
        List<Set<String[]>> list = new LinkedList<>();
        for(int i = 0; i < 2; i++) {
            list.add(randomSetOfStringArray());
        }
        return list;
    }

    private List<byte[]> fixedListOfByteArray() {
        List<byte[]> list = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            byte[] bytearray = new byte[10];
            list.add(bytearray);
        }
        return list;
    }

    private Set<String[]> randomSetOfStringArray() {
        Set<String[]> set = new HashSet<>();
        for(int i = 0; i < 2; i++) {
            set.add(randomStringArray());
        }
        return set;
    }

    private List<Set<B[]>> randomListOfSetOfBArray() {
        List<Set<B[]>> list = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            list.add(randomSetOfBArray());
        }
        return list;
    }

    private Set<B[]> randomSetOfBArray() {
        Set<B[]> set = new HashSet<>();
        for(int i = 0; i < 2; i++) {
            set.add(randomBArray());
        }
        return set;
    }

    private String[] randomStringArray() {
        String[] sArray = new String[3];
        for(int i = 0; i < sArray.length; i++) {
            sArray[i] = dataFactory.getEmailAddress();
        }
        return sArray;
    }

    private HashSet<List<B>>[] randomArrayOfSetOfBlist() {
        HashSet<List<B>>[] array = (HashSet<List<B>>[])Array.newInstance(HashSet.class, 2);
        for(int i = 0; i < array.length; i++) {
            array[i] = randomSetOfBlist();
        }
        return array;
    }

    private HashSet<List<B>> randomSetOfBlist() {
        HashSet<List<B>> set = new HashSet<>();
        for(int i = 0; i < 2; i++) {
            set.add(randomBList());
        }
        return set;
    }

    private List<B> randomBList() {
        return Arrays.asList(randomBArray());
    }

    private B[] randomBArray() {
        B[] bArray = new B[3];
        for(int i = 0; i < bArray.length; i++)
            bArray[i] = randomB();
        return bArray;
    }

    private B randomB() {
        String[] strings = new String[1];
        strings[0] = dataFactory.getEmailAddress();
        List<String[]> list = new ArrayList<>();
        list.add(strings);
        return new B(dataFactory.getEmailAddress(), list, LocalDate.of(2017, Month.NOVEMBER, 6));
    }

}
