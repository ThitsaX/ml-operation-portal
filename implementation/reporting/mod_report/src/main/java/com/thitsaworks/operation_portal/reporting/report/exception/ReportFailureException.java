package com.thitsaworks.operation_portal.reporting.report.exception;

import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;

import java.text.MessageFormat;

public class ReportFailureException extends OperationPortalException {

    public ReportFailureException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "REPORT_FAILURE_EXCEPTION";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("Failed to extract report : ({0})", this.params);

    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

}
