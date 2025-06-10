package com.thitsaworks.operation_portal.iam.exception;

import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;

public class UnauthorizedCreationException extends DFSPPortalException {

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
