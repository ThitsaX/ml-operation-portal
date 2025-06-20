package com.thitsaworks.operation_portal.component.misc.exception;

public abstract class IgnorableException extends OperationPortalException {

    public IgnorableException() {

        super();
    }

    public IgnorableException(String errorMessage) {

        super(errorMessage);
    }

}
