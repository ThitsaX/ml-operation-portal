package com.thitsaworks.operation_portal.core.participant.data;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;

import java.math.BigDecimal;

public record ParticipantNDCData(ParticipantNDCId participantNDCId,
                                 String dfspCode,
                                 String currency,
                                 BigDecimal ndcPercent,
                                 BigDecimal ndcAmount) {}
