package com.thitsaworks.operation_portal.component.misc.exception;

public class InputException extends SystemException {

    public InputException(ErrorMessage errorMessage) {

        super(errorMessage);
    }

    public InputException() {

        super();
    }

}