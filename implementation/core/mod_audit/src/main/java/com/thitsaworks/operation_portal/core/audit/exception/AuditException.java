package com.thitsaworks.operation_portal.core.audit.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class AuditException extends DomainException {

    public AuditException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}
