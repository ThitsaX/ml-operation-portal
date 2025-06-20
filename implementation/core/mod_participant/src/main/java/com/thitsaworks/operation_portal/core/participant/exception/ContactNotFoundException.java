package com.thitsaworks.operation_portal.core.participant.exception;


import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;

import java.text.MessageFormat;

public class ContactNotFoundException extends OperationPortalException {

    public ContactNotFoundException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "CONTACT_NOT_FOUND";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("System cannot find the contact with ID : ({0})", this.params);
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

    @Override
    public String paramDescription() {

        return "{0} : Contact ID.";
    }

}
