package com.exochain.data.account;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.fasterxml.uuid.impl.UUIDUtil;
import com.google.common.base.Objects;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Type safe identifier for a account id
 */
public class ExoAccountId implements Serializable{
    private static final TimeBasedGenerator UUID_GENERATOR = Generators.timeBasedGenerator();

    private final String value;

    public ExoAccountId() {
        this(UUID_GENERATOR.generate().toString());
    }

    /**
     * Creates a new account id from the value.
     *
     * @param value the value for the id, must be a valid 36 character UUID string
     */
    public ExoAccountId(String value) {
        // This parses string and throws NullPointerException or NumberFormatException if string invalid
        UUIDUtil.uuid(value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExoAccountId exoAccountId = (ExoAccountId) o;
        return Objects.equal(value, exoAccountId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

}
