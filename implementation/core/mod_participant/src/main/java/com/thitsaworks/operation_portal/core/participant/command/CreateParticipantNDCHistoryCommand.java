package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCHistoryId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;

public interface CreateParticipantNDCHistoryCommand {

    Output execute(Input input) throws ParticipantException;

    record Input(ParticipantNDC participantNDC) {}

    record Output(ParticipantNDCHistoryId participantNDCHistoryId) {}

}
