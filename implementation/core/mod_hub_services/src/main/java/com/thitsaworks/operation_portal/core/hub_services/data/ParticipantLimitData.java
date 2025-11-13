package com.thitsaworks.operation_portal.core.hub_services.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public record ParticipantLimitData(String participantCurrencyId,
                                   BigDecimal value,
                                   boolean isActive)
    implements Serializable { }
