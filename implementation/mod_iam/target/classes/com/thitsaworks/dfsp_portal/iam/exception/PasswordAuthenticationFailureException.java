package com.thitsaworks.dfsp_portal.iam.exception;

import com.thitsaworks.dfsp_portal.component.exception.IgnorableException;

public class PasswordAuthenticationFailureException extends IgnorableException {

    public PasswordAuthenticationFailureException() {

        super();
    }

    @Override
    public String errorCode() {

        return "PASSWORD_AUTHENTICATION_FAILURE";
    }

    @Override
    public String defaultErrorMessage() {

        return "Password authentication is failed.";
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

}
