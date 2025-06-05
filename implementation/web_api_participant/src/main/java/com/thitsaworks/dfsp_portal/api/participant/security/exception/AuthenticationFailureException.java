package com.thitsaworks.dfsp_portal.api.participant.security.exception;

public class AuthenticationFailureException extends AccessDeniedException {

    public AuthenticationFailureException() {

        super();
    }

    @Override
    public String errorCode() {

        return "AUTHENTICATION_FAILURE";
    }

    @Override
    public String defaultErrorMessage() {

        return "Authentication failure. Cannot reproduce Auth-Header at backend.";
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

}
