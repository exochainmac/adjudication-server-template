package com.exochain.axon.trace;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.Instant;

public class StandardTimeTraceInfo implements TimeTraceInfo {

    private Instant creationTime;
    private String creationTraceId;
    private String creationSpanId;

    private Instant modificationTime;
    private String modificationTraceId;
    private String modificationSpanId;

    public StandardTimeTraceInfo() {
        this.creationTime = Instant.now();
        this.modificationTime = Instant.now();
    }

    public StandardTimeTraceInfo(Instant creationTime, String creationTraceId, String creationSpanId,
                                 Instant modificationTime, String modificationTraceId,
                                 String modificationSpanId) {
        this.creationTime = creationTime;
        this.creationTraceId = creationTraceId;
        this.creationSpanId = creationSpanId;
        this.modificationTime = modificationTime;
        this.modificationTraceId = modificationTraceId;
        this.modificationSpanId = modificationSpanId;
    }

    @Override
    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String getCreationTraceId() {
        return creationTraceId;
    }

    public void setCreationTraceId(String creationTraceId) {
        this.creationTraceId = creationTraceId;
    }

    @Override
    public String getCreationSpanId() {
        return creationSpanId;
    }

    public void setCreationSpanId(String creationSpanId) {
        this.creationSpanId = creationSpanId;
    }

    @Override
    public Instant getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(Instant modificationTime) {
        this.modificationTime = modificationTime;
    }

    @Override
    public String getModificationTraceId() {
        return modificationTraceId;
    }

    public void setModificationTraceId(String modificationTraceId) {
        this.modificationTraceId = modificationTraceId;
    }

    @Override
    public String getModificationSpanId() {
        return modificationSpanId;
    }

    public void setModificationSpanId(String modificationSpanId) {
        this.modificationSpanId = modificationSpanId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("creationTime", creationTime)
                .add("creationTraceId", creationTraceId)
                .add("creationSpanId", creationSpanId)
                .add("modificationTime", modificationTime)
                .add("modificationTraceId", modificationTraceId)
                .add("modificationSpanId", modificationSpanId)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandardTimeTraceInfo that = (StandardTimeTraceInfo) o;
        return Objects.equal(creationTime, that.creationTime) &&
                Objects.equal(creationTraceId, that.creationTraceId) &&
                Objects.equal(creationSpanId, that.creationSpanId) &&
                Objects.equal(modificationTime, that.modificationTime) &&
                Objects.equal(modificationTraceId, that.modificationTraceId) &&
                Objects.equal(modificationSpanId, that.modificationSpanId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(creationTime, creationTraceId, creationSpanId, modificationTime, modificationTraceId, modificationSpanId);
    }


}
