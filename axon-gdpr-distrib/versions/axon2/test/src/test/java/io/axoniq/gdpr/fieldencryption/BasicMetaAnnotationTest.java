/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.PersonalData;
import lombok.Data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

/**
 * Runs the AbstractBasicTestSet against a class annotated with custom annotations.
 */
public class BasicMetaAnnotationTest extends AbstractBasicTestSet<BasicMetaAnnotationTest.PersonRegisteredEvent> {

    @Override
    public PersonRegisteredEvent createEvent(UUID id, String name, byte[] picture, String city) {
        return new PersonRegisteredEvent(id, name, picture, city);
    }

    @Override
    public UUID getId(PersonRegisteredEvent event) {
        return event.getId();
    }

    @Override
    public String getName(PersonRegisteredEvent event) {
        return event.getName();
    }

    @Override
    public byte[] getPicture(PersonRegisteredEvent event) {
        return event.getPicture();
    }

    @Override
    public String getCity(PersonRegisteredEvent event) {
        return event.getCity();
    }


    @DataSubjectId(group = "myGroup")
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface MyDataSubjectId {
    }

    @PersonalData(group = "myGroup")
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface MyPersonalData {
    }

    @Data
    public static class PersonRegisteredEvent {

        @MyDataSubjectId
        private final UUID id;

        @MyPersonalData
        private final String name;

        @PersonalData(group = "myGroup")
        private final byte[] picture;

        private final String city;
    }

}
