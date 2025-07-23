package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.util.List;
import java.util.Optional;

public interface ParticipantUserQuery {

    List<ParticipantUserData> getParticipantUsers(ParticipantId participantId);

    ParticipantUserData get(ParticipantUserId participantUserId) throws ParticipantException;

    Optional<ParticipantUserData> find(ParticipantUserId participantUserId);

    ParticipantUserData get(Email email) throws ParticipantException;

}

