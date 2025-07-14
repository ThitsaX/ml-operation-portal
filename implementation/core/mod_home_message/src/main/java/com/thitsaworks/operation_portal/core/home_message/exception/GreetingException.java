package com.thitsaworks.operation_portal.core.home_message.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;

public class GreetingException extends DomainException {

    public GreetingException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}
