package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantNDCData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNDCException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;

import java.util.Optional;

public interface ParticipantNDCQuery {

    ParticipantNDCData get(ParticipantNDCId participantNDCId) throws ParticipantNDCException;

    Optional<ParticipantNDC> get(String participantName, String currency);

}
