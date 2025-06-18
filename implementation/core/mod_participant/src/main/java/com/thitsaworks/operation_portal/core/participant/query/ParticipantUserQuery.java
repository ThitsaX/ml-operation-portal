package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.core.participant.exception.EmailNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;

import java.util.List;

public interface ParticipantUserQuery {

    List<ParticipantUserData> getParticipantUsers(ParticipantId participantId);

    ParticipantUserData get(ParticipantUserId participantUserId) throws ParticipantUserNotFoundException;

    ParticipantUserData get(Email email) throws EmailNotFoundException;

}

