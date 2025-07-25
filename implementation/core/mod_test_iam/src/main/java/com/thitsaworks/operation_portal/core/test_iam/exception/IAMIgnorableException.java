package com.thitsaworks.operation_portal.core.test_iam.exception;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import com.thitsaworks.operation_portal.component.misc.exception.IgnorableException;

public class IAMIgnorableException extends IgnorableException {

    public IAMIgnorableException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}
