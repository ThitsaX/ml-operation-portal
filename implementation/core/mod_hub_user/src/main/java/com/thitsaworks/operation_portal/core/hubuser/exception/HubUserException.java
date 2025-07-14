package com.thitsaworks.operation_portal.core.hubuser.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class HubUserException extends DomainException {

    public HubUserException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}
