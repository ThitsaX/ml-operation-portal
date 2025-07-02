package com.thitsaworks.operation_portal.reporting.report.domain;

import lombok.Value;

import java.time.Instant;

public interface GenerateFeeSettlementReportCommand {

    record Input(
            Instant startDate,
            Instant endDate,
            String fromFsp,
            String toFsp,
            String currency,
            String timezone,
            String fileType) {
    }

    record Output(byte[] rptData) {
    }

    Output execute(Input input) throws Exception;

}
