package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.util.List;

public interface ParticipantUserQuery {

    List<ParticipantUserData> getParticipantUsers(ParticipantId participantId);

    ParticipantUserData get(ParticipantUserId participantUserId) throws ParticipantException;

    ParticipantUserData get(Email email) throws ParticipantException;

}

