package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.util.List;
import java.util.Optional;

public interface ParticipantQuery {

    List<ParticipantData> getParticipants();

    ParticipantData get(ParticipantId participantId) throws ParticipantException;

    List<ParticipantData> getOtherParticipants(ParticipantId participantId);

    Optional<ParticipantData> get(String participantName);

}

