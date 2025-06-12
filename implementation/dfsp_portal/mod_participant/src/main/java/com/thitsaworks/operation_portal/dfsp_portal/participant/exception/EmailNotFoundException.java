package com.thitsaworks.operation_portal.dfsp_portal.participant.exception;


import com.thitsaworks.operation_portal.component.misc.exception.DFSPPortalException;

import java.text.MessageFormat;

public class EmailNotFoundException extends DFSPPortalException {

    public EmailNotFoundException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "EMAIL_NOT_FOUND";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("System cannot find the email : ({0})", this.params);
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

    @Override
    public String paramDescription() {

        return "{0} : Email.";
    }

}
