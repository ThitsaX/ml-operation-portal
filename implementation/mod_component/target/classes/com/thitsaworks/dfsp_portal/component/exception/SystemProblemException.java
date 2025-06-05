package com.thitsaworks.dfsp_portal.component.exception;

public class SystemProblemException extends DFSPPortalException {
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
