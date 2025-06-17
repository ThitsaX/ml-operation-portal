package com.thitsaworks.operation_portal.api.participant.security;

import com.thitsaworks.component.common.identifier.AccessKey;
import com.thitsaworks.component.common.identifier.ParticipantUserId;
import lombok.Value;

@Value
public class UserContext {

    ParticipantUserId participantUserId;

    AccessKey accessKey;

}
