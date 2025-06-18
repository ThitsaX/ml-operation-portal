package com.thitsaworks.operation_portal.usecase.central_ledger;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;

public abstract class GenerateDetailReport extends
                                           AbstractAuditableUseCase<GenerateDetailReport.Input, GenerateDetailReport.Output> {

    public record Input(
            String fspId,
            String settlementId,
            String fileType,
            String timezoneOffset
    ) {}

    public record Output(
            byte[] detailReportData
    ) {}

}
