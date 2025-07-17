package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GenerateDetailReport extends
                                      UseCase<GenerateDetailReport.Input, GenerateDetailReport.Output> {

    record Input(String fspId,
                 String settlementId,
                 String fileType,
                 String timezoneOffset
    ) { }

    record Output(byte[] detailReportData) { }

}
