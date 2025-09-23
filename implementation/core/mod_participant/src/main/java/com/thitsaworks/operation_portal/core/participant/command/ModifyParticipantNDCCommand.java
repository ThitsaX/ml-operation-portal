package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNDCException;

import java.math.BigDecimal;

public interface ModifyParticipantNDCCommand {

    Output execute(Input input) throws ParticipantNDCException;

    record Input(ParticipantNDCId participantNDCId,
                 BigDecimal ndcPercent) {}

    record Output(ParticipantNDCId participantNDCId) { }

}
