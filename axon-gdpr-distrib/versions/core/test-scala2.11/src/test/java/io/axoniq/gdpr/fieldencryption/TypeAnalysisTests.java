/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.*;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import io.axoniq.gdpr.cryptoengine.InMemoryCryptoEngine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.*;

public class TypeAnalysisTests {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CryptoEngine cryptoEngine;
    private FieldEncrypter fieldEncrypter;

    @Before
    public void setUp() throws Exception {
        cryptoEngine = new InMemoryCryptoEngine();
        fieldEncrypter = new FieldEncrypter(cryptoEngine);
    }

    @Test
    public void noConfigurationTest() throws Exception {
        Assert.assertFalse(fieldEncrypter.willProcess(new X()));
    }

    @Test
    public void correctConfigurationTests() throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        classes.addAll(Arrays.asList(this.getClass().getClasses()));
        Collections.reverse(classes);
        for(Class<?> clazz : classes) {
            if(clazz.getSimpleName().startsWith("OK")) {
                Assert.assertTrue(fieldEncrypter.willProcess(clazz.newInstance()));
                logger.info("Correctly processed {}", clazz.getSimpleName());
            }
        }
    }

    @Test
    public void badConfigurationTests() throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        classes.addAll(Arrays.asList(this.getClass().getClasses()));
        Collections.reverse(classes);
        for(Class<?> clazz : classes) {
            if (clazz.getSimpleName().startsWith("BAD")) {
                try {
                    Assert.assertTrue(fieldEncrypter.willProcess(clazz.newInstance()));
                    Assert.fail("Didn't receive correct exception for class " + clazz.getSimpleName());
                } catch (ConfigurationException ex) {
                    logger.info("Correct exception for class {}: {}", clazz.getSimpleName(), ex.getMessage());
                }
            }
        }
    }

    public static class X {
        String x;
    }

    public static class OK1 {
        @PersonalData String x;
        @PersonalData byte[] y;
    }

    public static class OK2 {
        @PersonalData List<String> x;
    }

    public static class OK3 {
        @PersonalData String[] x;
    }

    @PersonalDataType
    public static class A {
    }

    public static class OK4 {
        @DeepPersonalData List<A> x;
    }

    public static class OK5 {
        @PersonalData(scope = Scope.KEY)
        @DeepPersonalData(scope = Scope.VALUE)
        Map<String, ? extends OK4> x;
    }

    public interface B<X,Y,Z> extends Map<Y,Z> {
    }

    public static class OK6 {
        @DataSubjectId(scope = Scope.KEY)
        @DeepPersonalData(scope = Scope.VALUE)
        B<Object, String, A> b;
    }

    public static class OK7 {
        @PersonalData
        @PersonalData(group = "x")
        String x;
    }

    public static class OK8 {
        @PersonalData(scope = Scope.KEY)
        @PersonalData(scope = Scope.BOTH, group = "x")
        Map<String, String> x;
    }

    public static class BAD1 {
        @PersonalData LocalDate x;
    }

    public static class BAD2 {
        @DeepPersonalData LocalDate x;
    }

    public static class BAD3 {
        @DataSubjectId String x;
        @DataSubjectId String y;
    }

    public static class BAD4 {
        @PersonalData @DeepPersonalData String x;
    }

    public static class BAD5 {
        @SerializedPersonalData @DeepPersonalData String x;
    }

    public static class BAD6 {
        @PersonalData @DeepPersonalData @PersonalData Map x;
    }

    public static class BAD7 {
        @PersonalData(scope = Scope.VALUE) String x;
    }

    public static class BAD8 {
        @PersonalData Map x;
    }

    public static class BAD9 {
        @SerializedPersonalData LocalDate x;
    }

    public static class BAD10 {
        @SerializedPersonalData LocalDate x;
        LocalDate xEncrypted;
    }

    public static class BAD11 {
        @DataSubjectId(scope = Scope.VALUE) Map x;
    }

    public static class BAD12 {
        @DataSubjectId(scope = Scope.KEY) @PersonalData(scope = Scope.BOTH) Map<String, String> x;
    }

    public static class BAD13 {
        @SerializedPersonalData(storageField = "z") LocalDate x;
        @SerializedPersonalData(storageField = "z") LocalDate y;
        byte[] z;
    }

    public static class BAD14 {
        @PersonalData
        @PersonalData
        String x;
    }

    public static class BAD15 {
        @PersonalData(scope = Scope.KEY)
        @PersonalData(scope = Scope.BOTH)
        Map<String, String> x;
    }

    public static class BAD16 {
        @PersonalData(scope = Scope.VALUE)
        @PersonalData(scope = Scope.VALUE)
        Map<String, String> x;
    }
}
