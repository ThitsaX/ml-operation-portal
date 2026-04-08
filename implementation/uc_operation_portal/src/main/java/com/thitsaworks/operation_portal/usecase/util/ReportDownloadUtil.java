package com.thitsaworks.operation_portal.usecase.util;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;

import java.util.Locale;

public final class ReportDownloadUtil {

    private ReportDownloadUtil() {

    }

    public static String normalizeAllToken(String value) {

        if (value == null) {
            return "All";
        }

        return "all".equalsIgnoreCase(value.trim()) ? "All" : value.trim();
    }

    public static String normalizeFileType(String fileType) {

        return fileType == null ? "" : fileType.trim().toLowerCase(Locale.ROOT);
    }

    public static ErrorMessage resolveFailedError(String storedError, ErrorMessage defaultFailureError) {

        if (storedError == null || storedError.isBlank()) {
            return defaultFailureError;
        }

        int delimiterIndex = storedError.indexOf("-");
        String errorCode = delimiterIndex > 0 ? storedError.substring(0, delimiterIndex) : storedError;
        String errorDefaultMessage = delimiterIndex > 0 &&
            storedError.length() > delimiterIndex + 1 ? storedError.substring(delimiterIndex + 1) : "";

        return switch (errorCode) {
            case "RESULT_NOT_FOUND_EXCEPTION" -> withDefaultMessage(
                ReportErrors.RESULT_NOT_FOUND_EXCEPTION, errorDefaultMessage);
            case "FILE_FORMAT_NOT_ALLOWED_EXCEPTION" -> withDefaultMessage(
                ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION, errorDefaultMessage);
            case "REPORT_MAXIMUM_LIMIT_EXCEPTION" -> withDefaultMessage(
                ReportErrors.REPORT_MAXIMUM_LIMIT_EXCEPTION, errorDefaultMessage);
            case "AUDIT_REPORT_FAILURE_EXCEPTION" -> withDefaultMessage(
                ReportErrors.AUDIT_REPORT_FAILURE_EXCEPTION, errorDefaultMessage);
            case "TRANSACTION_DETAIL_REPORT_FAILURE_EXCEPTION" -> withDefaultMessage(
                ReportErrors.TRANSACTION_DETAIL_REPORT_FAILURE_EXCEPTION, errorDefaultMessage);
            case "SETTLEMENT_DETAIL_REPORT_FAILURE_EXCEPTION" -> withDefaultMessage(
                ReportErrors.SETTLEMENT_DETAIL_REPORT_FAILURE_EXCEPTION, errorDefaultMessage);
            case "SETTLEMENT_BANK_REPORT_FAILURE_EXCEPTION" -> withDefaultMessage(
                ReportErrors.SETTLEMENT_BANK_REPORT_FAILURE_EXCEPTION, errorDefaultMessage);
            case "SETTLEMENT_BANK_USECASE_REPORT_FAILURE_EXCEPTION" -> withDefaultMessage(
                ReportErrors.SETTLEMENT_BANK_USECASE_REPORT_FAILURE_EXCEPTION, errorDefaultMessage);
            case "SETTLEMENT_STATEMENT_REPORT_FAILURE_EXCEPTION" -> withDefaultMessage(
                ReportErrors.STATEMENT_REPORT_FAILURE_EXCEPTION, errorDefaultMessage);
            case "SETTLEMENT_AUDIT_REPORT_FAILURE_EXCEPTION" -> withDefaultMessage(
                ReportErrors.SETTLEMENT_AUDIT_REPORT_FAILURE_EXCEPTION, errorDefaultMessage);
            default -> defaultFailureError.defaultMessage(storedError);
        };
    }

    private static ErrorMessage withDefaultMessage(ErrorMessage errorMessage, String defaultMessage) {

        if (defaultMessage == null || defaultMessage.isBlank()) {
            return errorMessage;
        }

        return errorMessage.defaultMessage(defaultMessage);
    }
}
