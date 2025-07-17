package com.thitsaworks.operation_portal.core.hubuser.data;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;

public record GreetingData(
    GreetingId greetingId,
    String greetingTitle,
    String greetingDetail,
    boolean isDeleted
) {
}
