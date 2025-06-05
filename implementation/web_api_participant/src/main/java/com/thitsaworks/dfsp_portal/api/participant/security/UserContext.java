package com.thitsaworks.dfsp_portal.api.participant.security;

import com.thitsaworks.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import lombok.Value;

@Value
public class UserContext {

    ParticipantUserId participantUserId;

    AccessKey accessKey;

}
