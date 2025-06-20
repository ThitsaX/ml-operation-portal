package com.thitsaworks.operation_portal.core.hubuser.exception;


import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;

import java.text.MessageFormat;

public class HubUserNotFoundException extends OperationPortalException {

    public HubUserNotFoundException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "USER_NOT_FOUND";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("System cannot find the user with ID : ({0})", this.params);
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

    @Override
    public String paramDescription() {

        return "{0} : ParticipantUser ID.";
    }

}
