package com.thitsaworks.operation_portal.reporting.central_ledger.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class CentralLedgerException extends DomainException {

    public CentralLedgerException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}
