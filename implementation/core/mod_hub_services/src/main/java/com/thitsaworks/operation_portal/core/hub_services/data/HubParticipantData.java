package com.thitsaworks.operation_portal.core.hub_services.data;

import java.io.Serializable;
import java.time.Instant;

public record HubParticipantData(String participantId,
                                 String name,
                                 String description,
                                 boolean isActive,
                                 Instant createdDate,
                                 String createdBy) implements Serializable {

}
