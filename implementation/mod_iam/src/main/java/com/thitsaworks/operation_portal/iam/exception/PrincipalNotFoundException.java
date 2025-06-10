package com.thitsaworks.operation_portal.iam.exception;

import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;

public class PrincipalNotFoundException extends DFSPPortalException {

    public PrincipalNotFoundException() {

        super();
    }

    @Override
    public String errorCode() {

        return "PRINCIPAL_NOT_FOUND";
    }

    @Override
    public String defaultErrorMessage() {

        return "Principal is not found for the user.";
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

}
