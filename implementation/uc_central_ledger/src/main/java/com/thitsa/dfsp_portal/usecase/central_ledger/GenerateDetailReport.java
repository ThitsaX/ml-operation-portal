package com.thitsa.dfsp_portal.usecase.central_ledger;

import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class GenerateDetailReport extends
        AbstractAuditableUseCase<GenerateDetailReport.Input, GenerateDetailReport.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private String fspId;

        private String settlementId;

        private String fileType;

        private String timezoneOffset;


    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private byte[] detailReportData;

    }

}
