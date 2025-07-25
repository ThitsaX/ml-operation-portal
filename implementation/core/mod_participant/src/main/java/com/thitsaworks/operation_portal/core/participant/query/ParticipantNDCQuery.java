package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantNDCData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNDCException;

import java.util.Optional;

public interface ParticipantNDCQuery {

    ParticipantNDCData get(ParticipantNDCId participantNDCId) throws ParticipantNDCException;

    Optional<ParticipantNDCData> get(String dfspCode, String currency);

}
