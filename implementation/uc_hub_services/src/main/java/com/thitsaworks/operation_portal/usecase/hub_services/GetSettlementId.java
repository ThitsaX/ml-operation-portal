package com.thitsaworks.operation_portal.usecase.hub_services;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.reporting.report.domain.data.SettlementIdData;

import java.time.Instant;
import java.util.List;

public interface GetSettlementId extends
                                 UseCase<GetSettlementId.Input, GetSettlementId.Output> {

    record Input(Instant startDate,
                 Instant endDate,
                 String timezoneOffset) { }

    record Output(List<SettlementIdData> settlementIds) { }

}
