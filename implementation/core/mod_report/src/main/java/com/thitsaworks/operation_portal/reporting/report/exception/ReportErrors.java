package com.thitsaworks.operation_portal.reporting.report.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class ReportErrors {

    //@@formatter:off
    public static final ErrorMessage REPORT_FAILURE_EXCEPTION = new ErrorMessage("REPORT_FAILURE_EXCEPTION", "Failed to extract report.");
    public static final ErrorMessage AUDIT_REPORT_FAILURE_EXCEPTION = new ErrorMessage("AUDIT_REPORT_FAILURE_EXCEPTION", "Failed to extract audit report.");
    public static final ErrorMessage DAILY_TRANSACTION_SUMMARY_REPORT_FAILURE_EXCEPTION = new ErrorMessage("DAILY_TRANSACTION_SUMMARY_REPORT_FAILURE_EXCEPTION", "Failed to extract daily transaction trend summary report.");
    public static final ErrorMessage SETTLEMENT_AUDIT_REPORT_FAILURE_EXCEPTION = new ErrorMessage("SETTLEMENT_AUDIT_REPORT_FAILURE_EXCEPTION", "Failed to extract settlement audit report.");
    public static final ErrorMessage SETTLEMENT_DETAIL_REPORT_FAILURE_EXCEPTION = new ErrorMessage("SETTLEMENT_DETAIL_REPORT_FAILURE_EXCEPTION", "Failed to extract settlement detail report.");
    public static final ErrorMessage FEE_SETTLEMENT_REPORT_FAILURE_EXCEPTION = new ErrorMessage("FEE_SETTLEMENT_REPORT_FAILURE_EXCEPTION", "Failed to extract fee settlement report.");
    public static final ErrorMessage STATEMENT_REPORT_FAILURE_EXCEPTION = new ErrorMessage("STATEMENT_REPORT_FAILURE_EXCEPTION", "Failed to extract statement report.");

    public static final ErrorMessage FILE_FORMAT_NOT_ALLOWED = new ErrorMessage("FILE_FORMAT_NOT_ALLOWED", "The file format is required or the requested file format is not allowed.");
    public static final ErrorMessage RESULT_NOT_FOUND = new ErrorMessage("RESULT_NOT_FOUND", "The results for the selected periods or criteria were not found.");
    public static final ErrorMessage SETTLEMENT_REPORT_FAILURE_EXCEPTION = new ErrorMessage("SETTLEMENT_REPORT_FAILURE_EXCEPTION", "Failed to extract settlement Fee report.");

    //@@formatter:on
}
