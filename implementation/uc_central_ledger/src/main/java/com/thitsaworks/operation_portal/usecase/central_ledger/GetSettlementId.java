package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;
import com.thitsaworks.operation_portal.reporting.report.domain.data.SettlementIdData;
import java.time.Instant;
import java.util.List;

public abstract class GetSettlementId extends
        AbstractOwnableUseCase<GetSettlementId.Input, GetSettlementId.Output> {

    public record Input(Instant startDate, Instant endDate, String timezoneOffset) {}

    public record Output(List<SettlementIdData> settlementIds) {}
}
