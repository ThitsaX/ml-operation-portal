package com.thitsaworks.operation_portal.core.approval.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class ApprovalException extends DomainException {

    public ApprovalException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}
