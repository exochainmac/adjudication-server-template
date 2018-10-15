/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.fieldencryption;

import io.axoniq.gdpr.api.DataSubjectId;
import io.axoniq.gdpr.api.PersonalData;

import java.util.Arrays;
import java.util.UUID;

/**
 * Runs the AbstractBasicTestSet against a vanilla Java class.
 */
public class BasicVanillaTest extends AbstractBasicTestSet<BasicVanillaTest.PersonRegisteredEvent> {

    @Override
    public BasicVanillaTest.PersonRegisteredEvent createEvent(UUID id, String name, byte[] picture, String city) {
        return new BasicVanillaTest.PersonRegisteredEvent(id, name, picture, city);
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

    public static class PersonRegisteredEvent {

        @DataSubjectId
        private final UUID id;

        @PersonalData
        private final String name;

        @PersonalData
        private final byte[] picture;

        private final String city;

        public PersonRegisteredEvent(UUID id, String name, byte[] picture, String city) {
            this.id = id;
            this.name = name;
            this.picture = picture;
            this.city = city;
        }

        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public byte[] getPicture() {
            return picture;
        }

        public String getCity() {
            return city;
        }

        @Override
        public String toString() {
            return "PersonRegisteredEvent{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", picture=" + Arrays.toString(picture) +
                    ", city='" + city + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PersonRegisteredEvent that = (PersonRegisteredEvent) o;

            if (id != null ? !id.equals(that.id) : that.id != null) return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (!Arrays.equals(picture, that.picture)) return false;
            return city != null ? city.equals(that.city) : that.city == null;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + Arrays.hashCode(picture);
            result = 31 * result + (city != null ? city.hashCode() : 0);
            return result;
        }
    }


}
