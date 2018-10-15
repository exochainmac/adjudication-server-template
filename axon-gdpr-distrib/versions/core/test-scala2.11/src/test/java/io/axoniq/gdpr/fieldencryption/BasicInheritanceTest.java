/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.PersonalData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

/**
 * Runs the AbstractBasicTestSet against a class derived from a parentclass. (Using Lombok for convenience.)
 */
public class BasicInheritanceTest extends AbstractBasicTestSet<BasicInheritanceTest.PersonRegisteredEvent> {

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
    public static class PersonEvent {

        @DataSubjectId
        private final UUID id;

        @PersonalData
        private final String name;

    }

    @ToString(callSuper=true) @EqualsAndHashCode @Getter
    public static class PersonRegisteredEvent extends PersonEvent {

        @PersonalData
        private final byte[] picture;

        private final String city;

        public PersonRegisteredEvent(UUID id, String name, byte[] picture, String city) {
            super(id, name);
            this.picture = picture;
            this.city = city;
        }
    }

}
