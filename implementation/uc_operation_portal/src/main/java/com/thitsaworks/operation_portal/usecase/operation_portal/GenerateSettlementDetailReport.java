package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GenerateSettlementDetailReport extends
                                      UseCase<GenerateSettlementDetailReport.Input, GenerateSettlementDetailReport.Output> {

    record Input(String fspId,
                 String settlementId,
                 String fileType,
                 String timezoneOffset
    ) { }

    record Output(byte[] detailReportData) { }

}
