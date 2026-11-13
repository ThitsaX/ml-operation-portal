package com.thitsaworks.operation_portal.core.notification.command;

import com.thitsaworks.operation_portal.component.common.identifier.NdcAlertEventId;
import com.thitsaworks.operation_portal.component.common.type.NdcThresholdStateType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;

import java.math.BigDecimal;
import java.time.Instant;

public interface EvaluateNdcThresholdCommand {

    record Input(
        String participantName,
        String dfspDisplayName,
        String currency,
        BigDecimal currentPosition,
        BigDecimal ndcLimit,
        BigDecimal currentNdcUsed,
        BigDecimal thresholdPercent,
        Instant evaluatedAt,
        String actor
    ) {}

    record Output(
        NdcThresholdStateType previousState,
        NdcThresholdStateType currentState,
        long breachCycleNo,
        boolean alertCreated,
        boolean recovered,
        NdcAlertEventId alertEventId
    ) {}

    Output execute(Input input) throws DomainException;
}
