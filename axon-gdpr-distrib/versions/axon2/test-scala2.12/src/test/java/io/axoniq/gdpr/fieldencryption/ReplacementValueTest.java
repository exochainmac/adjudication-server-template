/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.*;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import io.axoniq.gdpr.cryptoengine.InMemoryCryptoEngine;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Tests of the custom replacement value mechanism.
 */
public class ReplacementValueTest {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CryptoEngine cryptoEngine;
    private FieldEncrypter fieldEncrypter;
    private DataFactory dataFactory;

    public static class CustomReplacementValueProviderDeleted extends ReplacementValueProvider {
        @Override
        public Object replacementValue(Class<?> clazz, Field field, Type fieldType, String groupName, String replacement, byte[] storedPartialValue) {
            if(LocalDate.class.equals(fieldType) && "*YEARONLY*".equals(replacement) && storedPartialValue != null) {
                ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
                buffer.put(storedPartialValue);
                buffer.flip();
                return LocalDate.of(buffer.getInt(), Month.JANUARY, 1);
            } else if(field.getName().equals("name")) {
                return "<deleted name>";
            } else {
                return super.replacementValue(clazz, field, fieldType, groupName, replacement, storedPartialValue);
            }
        }

        @Override
        public byte[] partialValueForStorage(Class<?> clazz, Field field, Type fieldType, String groupName, String replacement, Object inputValue) {
            if(LocalDate.class.equals(fieldType) && "*YEARONLY*".equals(replacement) && inputValue instanceof LocalDate) {
                ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
                buffer.putInt(((LocalDate)inputValue).getYear());
                return buffer.array();
            } else {
                return super.partialValueForStorage(clazz, field, fieldType, groupName, replacement, inputValue);
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        cryptoEngine = new InMemoryCryptoEngine();
        fieldEncrypter = new FieldEncrypter(cryptoEngine, new CustomReplacementValueProviderDeleted());
        dataFactory = new DataFactory();
        dataFactory.randomize(ThreadLocalRandom.current().nextInt());
    }

    @Test
    public void customReplacementValueProviderMustBeEffective() {
        Person person = new Person(ThreadLocalRandom.current().nextLong(), dataFactory.getLastName(), dataFactory.getAddress(),
                Arrays.asList(dataFactory.getFirstName(), dataFactory.getFirstName()),
                LocalDate.of(1977, Month.MARCH, 9));
        fieldEncrypter.encrypt(person);
        cryptoEngine.deleteKey(Long.toString(person.id));
        fieldEncrypter.decrypt(person);
        logger.info("person after decryption without key: {}", person);
        Assert.assertEquals("<deleted name>", person.name);
        Assert.assertEquals("<deleted address>", person.address);
        Assert.assertEquals("<unknown kid>", person.kids.get(0));
        Assert.assertEquals("<unknown kid>", person.kids.get(1));
        Assert.assertEquals(LocalDate.of(1977, Month.JANUARY, 1), person.dateOfBirth);
    }

    @Test
    public void customReplacementValueProviderMustBeEffectiveWithDirectReplacement() {
        Person person = new Person(ThreadLocalRandom.current().nextLong(), dataFactory.getLastName(), dataFactory.getAddress(),
                Arrays.asList(dataFactory.getFirstName(), dataFactory.getFirstName()),
                LocalDate.of(1977, Month.MARCH, 9));
        fieldEncrypter.replace(person);
        logger.info("person after replacement: {}", person);
        Assert.assertEquals("<deleted name>", person.name);
        Assert.assertEquals("<deleted address>", person.address);
        Assert.assertEquals("<unknown kid>", person.kids.get(0));
        Assert.assertEquals("<unknown kid>", person.kids.get(1));
        Assert.assertEquals(LocalDate.of(1977, Month.JANUARY, 1), person.dateOfBirth);
    }

    public static class Person {

        @DataSubjectId
        private final long id;

        @PersonalData
        private final String name;

        @PersonalData(replacement = "<deleted address>")
        private final String address;

        @PersonalData(replacement = "<unknown kid>")
        private final List<String> kids;

        @SerializedPersonalData(replacement = "*YEARONLY*")
        private final LocalDate dateOfBirth;
        private byte[] dateOfBirthEncrypted;

        public Person(long id, String name, String address, List<String> kids, LocalDate dateOfBirth) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.kids = kids;
            this.dateOfBirth = dateOfBirth;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    ", kids=" + kids +
                    ", dateOfBirth=" + dateOfBirth +
                    '}';
        }
    }

}
