/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.PersonalData;
import lombok.Data;

import java.util.UUID;

/**
 * Runs the AbstractBasicTestSet against a Lombok-annotated Java class.
 */
public class BasicLombokTest extends AbstractBasicTestSet<BasicLombokTest.PersonRegisteredEvent> {

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

    @Data
    public static class PersonRegisteredEvent {

        @DataSubjectId
        private final UUID id;

        @PersonalData
        private final String name;

        @PersonalData
        private final byte[] picture;

        private final String city;
    }

}
