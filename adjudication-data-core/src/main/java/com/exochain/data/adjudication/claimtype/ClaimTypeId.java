package com.exochain.data.adjudication.claimtype;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Type safe identifier for a claim type
 */
public class ClaimTypeId implements Serializable{
    private static final TimeBasedGenerator UUID_GENERATOR = Generators.timeBasedGenerator();

    private final String value;

    public ClaimTypeId() {
        this(UUID_GENERATOR.generate().toString());
    }

    public ClaimTypeId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClaimTypeId that = (ClaimTypeId) o;
        return Objects.equal(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
