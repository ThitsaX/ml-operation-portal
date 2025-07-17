package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantNDCData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNDCException;

public interface ParticipantNDCQuery {

    ParticipantNDCData get(ParticipantNDCId participantNDCId) throws ParticipantNDCException;

}
