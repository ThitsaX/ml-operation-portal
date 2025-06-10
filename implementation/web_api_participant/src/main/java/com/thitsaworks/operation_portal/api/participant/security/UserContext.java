package com.thitsaworks.operation_portal.api.participant.security;

import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import lombok.Value;

@Value
public class UserContext {

    ParticipantUserId participantUserId;

    AccessKey accessKey;

}
