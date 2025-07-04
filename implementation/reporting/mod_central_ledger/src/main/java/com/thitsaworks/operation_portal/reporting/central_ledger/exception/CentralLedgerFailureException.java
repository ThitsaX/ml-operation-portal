package com.thitsaworks.operation_portal.reporting.central_ledger.exception;

import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;

import java.text.MessageFormat;

public class CentralLedgerFailureException extends OperationPortalException {

    public CentralLedgerFailureException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "CENTRAL_LEDGER_FAILURE_EXCEPTION";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("Central Ledger Failure : ({0})", this.params);

    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

}
