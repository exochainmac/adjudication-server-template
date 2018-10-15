package com.exochain.axon.trace;

import java.time.Instant;

public final class StandardTimeTraceInfoBuilder {
    private StandardTimeTraceInfo standardTimeTraceInfo;

    private StandardTimeTraceInfoBuilder() {
        standardTimeTraceInfo = new StandardTimeTraceInfo();
    }

    public static StandardTimeTraceInfoBuilder aStandardTimeTraceInfo() {
        return new StandardTimeTraceInfoBuilder();
    }

    public StandardTimeTraceInfoBuilder withCreationTime(Instant creationTime) {
        standardTimeTraceInfo.setCreationTime(creationTime);
        return this;
    }

    public StandardTimeTraceInfoBuilder withCreationTraceId(String creationTraceId) {
        standardTimeTraceInfo.setCreationTraceId(creationTraceId);
        return this;
    }

    public StandardTimeTraceInfoBuilder withCreationSpanId(String creationSpanId) {
        standardTimeTraceInfo.setCreationSpanId(creationSpanId);
        return this;
    }

    public StandardTimeTraceInfoBuilder withModificationTime(Instant modificationTime) {
        standardTimeTraceInfo.setModificationTime(modificationTime);
        return this;
    }

    public StandardTimeTraceInfoBuilder withModificationTraceId(String modificationTraceId) {
        standardTimeTraceInfo.setModificationTraceId(modificationTraceId);
        return this;
    }

    public StandardTimeTraceInfoBuilder withModificationSpanId(String modificationSpanId) {
        standardTimeTraceInfo.setModificationSpanId(modificationSpanId);
        return this;
    }

    public StandardTimeTraceInfoBuilder but() {
        return aStandardTimeTraceInfo().withCreationTime(standardTimeTraceInfo.getCreationTime()).withCreationTraceId(standardTimeTraceInfo.getCreationTraceId()).withCreationSpanId(standardTimeTraceInfo.getCreationSpanId()).withModificationTime(standardTimeTraceInfo.getModificationTime()).withModificationTraceId(standardTimeTraceInfo.getModificationTraceId()).withModificationSpanId(standardTimeTraceInfo.getModificationSpanId());
    }

    public StandardTimeTraceInfo build() {
        return standardTimeTraceInfo;
    }
}
