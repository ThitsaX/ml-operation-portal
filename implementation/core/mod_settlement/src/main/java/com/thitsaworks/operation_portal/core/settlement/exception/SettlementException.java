package com.thitsaworks.operation_portal.core.settlement.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class SettlementException extends DomainException {

    public SettlementException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}
