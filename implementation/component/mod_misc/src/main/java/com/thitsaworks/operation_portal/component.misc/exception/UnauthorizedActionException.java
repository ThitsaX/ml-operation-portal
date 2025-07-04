package com.thitsaworks.operation_portal.component.misc.exception;

import java.text.MessageFormat;

public class UnauthorizedActionException extends OperationPortalException {

    public UnauthorizedActionException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "UNAUTHORIZED_ACTION";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("You are not allowed to perform this action {0}.", this.params);
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

}
