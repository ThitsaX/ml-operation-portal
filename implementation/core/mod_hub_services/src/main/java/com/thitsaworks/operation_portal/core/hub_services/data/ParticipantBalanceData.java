package com.thitsaworks.operation_portal.core.hub_services.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public record ParticipantBalanceData(String currency,
                                     String ledgerAccountType,
                                     BigDecimal value,
                                     BigDecimal reservedValue,
                                     boolean isActive,
                                     Instant changedDate)
        implements Serializable {}
