/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.serialization;

import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.FieldEncryptingSerializer;
import io.axoniq.gdpr.api.PersonalData;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import io.axoniq.gdpr.cryptoengine.InMemoryCryptoEngine;
import io.axoniq.gdpr.utils.TestUtils;
import org.axonframework.serializer.SerializedObject;
import org.axonframework.serializer.Serializer;
import org.axonframework.serializer.xml.XStreamSerializer;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ThreadLocalRandom;

public class BasicSerializationTest {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CryptoEngine cryptoEngine;
    private Serializer serializer;
    private DataFactory dataFactory;

    @Before
    public void setUp() throws Exception {
        cryptoEngine = new InMemoryCryptoEngine();
        serializer = new FieldEncryptingSerializer(cryptoEngine, new XStreamSerializer());
        dataFactory = new DataFactory();
        dataFactory.randomize(ThreadLocalRandom.current().nextInt());
    }

    @Test
    public void serializationMustDoEncryption() {
        Person person = new Person(ThreadLocalRandom.current().nextLong(), dataFactory.getLastName());
        logger.info("Person before serialization: {}", person);
        String originalName = person.name;
        SerializedObject<String> serialized = serializer.serialize(person, String.class);
        logger.info("Serialized form: {}", serialized.getData());
        Assert.assertFalse(serialized.getData().contains(originalName));
        Assert.assertTrue(serialized.getData().contains(TestUtils.ENCRYPTED_STRING_START));
    }

    @Test
    public void serializationMustBeReversible() {
        Person person = new Person(ThreadLocalRandom.current().nextLong(), dataFactory.getLastName());
        SerializedObject<String> serialized = serializer.serialize(person, String.class);
        Person newPerson = serializer.deserialize(serialized);
        Assert.assertEquals(person, newPerson);
    }

    public static class Person {

        @DataSubjectId
        private final long id;

        @PersonalData
        private final String name;

        public Person(long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            if (id != person.id) return false;
            return name != null ? name.equals(person.name) : person.name == null;
        }

        @Override
        public int hashCode() {
            int result = (int) (id ^ (id >>> 32));
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }

}
