package com.thitsaworks.operation_portal.component.misc.exception;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class DomainException extends Exception {

    @Getter(AccessLevel.PUBLIC)
    private final ErrorMessage errorMessage;

    public DomainException(ErrorMessage errorMessage) {

        super(errorMessage.code() + "-" + errorMessage.description());
        this.errorMessage = errorMessage;
    }

    public DomainException() {

        super();
        this.errorMessage = null;
    }

}