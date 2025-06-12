package com.thitsaworks.operation_portal.dfsp_portal.participant.exception;

import com.thitsaworks.operation_portal.component.misc.exception.DFSPPortalException;

import java.text.MessageFormat;

public class ParticipantUserNotFoundException extends DFSPPortalException {

    public ParticipantUserNotFoundException(String params) {

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
