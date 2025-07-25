package com.thitsaworks.operation_portal.core.hub_services.data;

import java.io.Serializable;
import java.math.BigDecimal;

public record FinancialData(String dfspId,
                            String dfspName,
                            String currency,
                            BigDecimal balance,
                            BigDecimal currentPosition,
                            BigDecimal ndcPercent,
                            BigDecimal ndc,
                            BigDecimal ndcUsed,
                            Integer participantSettlementCurrencyId,

                            Integer participantPositionCurrencyId) implements Serializable {}
