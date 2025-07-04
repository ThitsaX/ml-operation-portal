package com.thitsaworks.operation_portal.core.iam.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class IAMException extends DomainException {

    public IAMException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}
