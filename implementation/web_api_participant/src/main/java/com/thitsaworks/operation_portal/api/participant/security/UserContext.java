package com.thitsaworks.operation_portal.api.participant.security;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import lombok.Value;

@Value
public class UserContext {

    ParticipantUserId participantUserId;

    AccessKey accessKey;

}
