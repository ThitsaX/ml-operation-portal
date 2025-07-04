package com.thitsaworks.operation_portal.reporting.report.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class ReportException extends DomainException {

    public ReportException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}
