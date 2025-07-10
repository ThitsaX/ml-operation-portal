package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.io.OutputStream;

public interface GenerateDfspDailyTransactionTrendSummaryCommand {

    record Input(
            String fspId, String startdate, String enddate, String fileType,
            OutputStream destination) {
    }

    record Output(Boolean generated) {
    }

    Output execute(Input input) throws ReportException;

}
