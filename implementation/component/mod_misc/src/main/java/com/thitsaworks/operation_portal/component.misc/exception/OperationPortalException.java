package com.thitsaworks.operation_portal.component.misc.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class OperationPortalException extends Exception {

    protected String params;

    protected OperationPortalException(String params) {

        super();
        this.params = params;
    }

    public abstract String errorCode();

    public abstract String defaultErrorMessage();

    public abstract boolean requireTranslation();

    public String paramDescription() {

        return "No params required.";
    }

}
