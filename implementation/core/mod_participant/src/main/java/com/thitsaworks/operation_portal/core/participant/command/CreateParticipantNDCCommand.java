package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;

import java.math.BigDecimal;

public interface CreateParticipantNDCCommand {

    Output execute(Input input);

    record Input(String dfspCode,
                 String currency,
                 BigDecimal ndcPercent,
                 BigDecimal ndcAmount) { }

    record Output(ParticipantNDCId participantNDCId) { }

}
