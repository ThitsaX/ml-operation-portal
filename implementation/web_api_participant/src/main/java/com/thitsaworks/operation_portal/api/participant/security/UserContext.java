package com.thitsaworks.operation_portal.api.participant.security;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;

public record UserContext(ParticipantUserId participantUserId, AccessKey accessKey) {
}
