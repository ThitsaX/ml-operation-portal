package com.thitsaworks.operation_portal.core.participant.data;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;

import java.io.Serializable;
import java.math.BigDecimal;

public record ParticipantNDCData(ParticipantNDCId participantNDCId,
                                 String dfspCode,
                                 String currency,
                                 BigDecimal ndcPercent) implements Serializable {

    public ParticipantNDCData(ParticipantNDC participantNDC) {

        this(participantNDC.getParticipantNDCId(),
             participantNDC.getDfspCode(),
             participantNDC.getCurrency(),
             participantNDC.getNdcPercent());
    }

}
