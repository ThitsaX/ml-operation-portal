package com.thitsaworks.operation_portal.component.misc.exception;

import lombok.AccessLevel;
import lombok.Getter;

public class SystemException extends RuntimeException {

    @Getter(AccessLevel.PUBLIC)
    private final ErrorMessage errorMessage;

    public SystemException(ErrorMessage errorMessage) {

        super(errorMessage.code() + "-" + errorMessage.description());
        this.errorMessage = errorMessage;
    }

    public SystemException() {

        super();
        this.errorMessage = null;
    }

}