package com.thitsaworks.operation_portal.core.hub_services.data;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;

import java.io.Serializable;
import java.math.BigDecimal;

public record FinancialData(ParticipantId participantId,
                            String dfspId,
                            String dfspName,
                            String currency,
                            BigDecimal balance,
                            BigDecimal currentPosition,
                            BigDecimal ndcPercent,
                            BigDecimal ndc,
                            BigDecimal ndcUsed,
                            Integer participantSettlementCurrencyId,

                            Integer participantPositionCurrencyId) implements Serializable {}
