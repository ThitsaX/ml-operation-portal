package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCHistoryId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;

import java.math.BigDecimal;

public interface CreateParticipantNDCHistoryCommand {

    Output execute(Input input) throws ParticipantException;

    record Input(ParticipantNDC participantNDC,
                 String dfspCode,
                 String currency,
                 BigDecimal ndcPercent) {}

    record Output(ParticipantNDCHistoryId participantNDCHistoryId) {}

}
