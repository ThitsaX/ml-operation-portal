package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.reporting.report.domain.data.SettlementIdData;

import java.time.Instant;
import java.util.List;

public interface GetSettlementIdWithParentParticipant extends
        UseCase<GetSettlementIdWithParentParticipant.Input, GetSettlementIdWithParentParticipant.Output> {

    record Input(Instant startDate,
                 Instant endDate,
                 Integer dfspId,
                 String timezoneOffset) { }

    record Output(List<SettlementIdData> settlementIds) { }
}
