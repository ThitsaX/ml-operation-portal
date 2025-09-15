package com.thitsaworks.operation_portal.core.participant.data;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;

import java.time.Instant;

public record GreetingData(
    GreetingId greetingId,
    String greetingTitle,
    String greetingDetail,
    boolean isDeleted,
    Instant greetDate
) {
}
