package com.thitsaworks.operation_portal.iam.exception;

import com.thitsaworks.operation_portal.component.exception.IgnorableException;

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
