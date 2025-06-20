package com.thitsaworks.operation_portal.component.exception;

import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;

public class SystemProblemException extends OperationPortalException {
    public SystemProblemException() {

        super();
    }

    public SystemProblemException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "SYSTEM_PROBLEM";
    }

    @Override
    public String defaultErrorMessage() {

        return String.format("Something went wrong. Error : {0}", this.params);
    }

    @Override
    public boolean requireTranslation() {

        return false;
    }

}
