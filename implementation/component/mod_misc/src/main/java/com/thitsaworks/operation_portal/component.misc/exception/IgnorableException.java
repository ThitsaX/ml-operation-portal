package com.thitsaworks.operation_portal.component.misc.exception;

public abstract class IgnorableException extends DomainException {

    public IgnorableException() {

        super();
    }

    public IgnorableException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

}
