package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;

import java.math.BigDecimal;

public interface ModifyParticipantNDCCommand {

    Output execute(Input input);

    record Input(ParticipantNDCId participantNDCId,
                 BigDecimal ndcPercent) { }

    record Output(ParticipantNDCId participantNDCId) { }

}
