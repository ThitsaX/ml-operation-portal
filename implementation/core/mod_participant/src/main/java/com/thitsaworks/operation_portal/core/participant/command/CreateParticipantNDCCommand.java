package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;

import java.math.BigDecimal;

public interface CreateParticipantNDCCommand {

    Output execute(Input input);

    record Input(String dfsp,
                 String currency,
                 BigDecimal ndcPercent) { }

    record Output(ParticipantNDCId participantNDCId) { }

}
