package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface GenerateFeeSettlementReport extends
                                             UseCase<GenerateFeeSettlementReport.Input, GenerateFeeSettlementReport.Output> {

    record Input(Instant startDate,
                 Instant endDate,
                 String fromFsp,
                 String toFsp,
                 String currency,
                 String timezone,
                 String fileType) { }

    record Output(byte[] rptData) { }

}
