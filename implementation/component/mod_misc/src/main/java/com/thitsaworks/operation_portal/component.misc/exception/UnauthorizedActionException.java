package com.thitsaworks.operation_portal.component.misc.exception;

public class UnauthorizedActionException extends DomainException {

    public UnauthorizedActionException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

    public UnauthorizedActionException() {

        super();
    }

}
