package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;

import java.util.List;

public interface ParticipantQuery {

    List<ParticipantData> getParticipants();

    ParticipantData get(ParticipantId participantId) throws ParticipantNotFoundException;

    List<ParticipantData> getOtherParticipants(ParticipantId participantId) throws ParticipantNotFoundException;

}

