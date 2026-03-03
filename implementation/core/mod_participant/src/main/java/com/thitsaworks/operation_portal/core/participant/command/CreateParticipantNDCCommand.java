package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.math.BigDecimal;

public interface CreateParticipantNDCCommand {

    Output execute(Input input) throws ParticipantException;

    record Input(String participantName,
                 String currency,
                 BigDecimal ndcPercent,
                 BigDecimal ndcAmount,
                 BigDecimal balance,
                 String madeBy) {}

    record Output(ParticipantNDCId participantNDCId) { }

}
