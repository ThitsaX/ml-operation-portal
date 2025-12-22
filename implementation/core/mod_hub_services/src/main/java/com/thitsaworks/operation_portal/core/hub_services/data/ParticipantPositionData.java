package com.thitsaworks.operation_portal.core.hub_services.data;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;

import java.io.Serializable;
import java.math.BigDecimal;

public record ParticipantPositionData( String dfspId,
                                      String dfspName,
                                      String currency,
                                      Integer participantSettlementCurrencyId,
                                      Integer participantPositionCurrencyId,
                                      boolean isActive) implements Serializable {}
