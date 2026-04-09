package com.thitsaworks.operation_portal.reporting.report.domain;

import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;

public interface GenerateSettlementBankOverviewReportCommand {

    record Input(String settlementId,
                 String currencyId,
                 String fileType,
                 String timezoneOffset,
                 String userName,
                 String dfspId,
                 boolean isParent,
                 Integer offset,
                 Integer limit) {

        public Input(String settlementId,
                     String currencyId,
                     String fileType,
                     String timezoneOffset,
                     String userName,
                     String dfspId,
                     boolean isParent) {

            this(settlementId, currencyId, fileType, timezoneOffset, userName, dfspId, isParent, null, null);
        }
    }

    record Output(byte[] settlementBankRptByte) { }

    Output execute(Input input) throws ReportException;

    record CountInput(String settlementId, String currencyId, String dfspId) { }

    default int countRows(CountInput input) {
        return 0;
    }

}
