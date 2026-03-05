package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

import java.time.Instant;

public interface GenerateTransactionDetailReportCommand {

    record CountInput(Instant startDate,
                      Instant endDate,
                      String state,
                      String dfspId,
                      String timeZoneOffset) { }

    record Input(Instant startDate,
                 Instant endDate,
                 String state,

                 String dfspId,
                 String filetype,
                 String timeZoneOffset,
                 Integer offset,
                 Integer limit) { }

    record Output(byte[] transactionDetailRptByte) { }

    Output execute(Input input) throws ReportException;

    int countRows(CountInput input);

    int transactionPageSize();

}
