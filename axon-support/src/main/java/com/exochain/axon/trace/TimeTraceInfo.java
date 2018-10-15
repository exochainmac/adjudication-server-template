package com.exochain.axon.trace;

import java.time.Instant;

public interface TimeTraceInfo {

    Instant getCreationTime();
    String getCreationTraceId();
    String getCreationSpanId();

    Instant getModificationTime();
    String getModificationTraceId();
    String getModificationSpanId();
}
