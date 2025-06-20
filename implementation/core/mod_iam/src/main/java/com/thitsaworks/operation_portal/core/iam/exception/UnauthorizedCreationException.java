package com.thitsaworks.operation_portal.core.iam.exception;

import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;

public class UnauthorizedCreationException extends OperationPortalException {

    public UnauthorizedCreationException() {

        super();
    }

    @Override
    public String errorCode() {

        return "UNAUTHORIZED_CREATION";
    }

    @Override
    public String defaultErrorMessage() {

        return "Unauthorized creation for other participant's user.";
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

}
