package com.thitsaworks.operation_portal.core.participant.exception;


import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;

import java.text.MessageFormat;

public class ParticipantAlreadyRegisteredException extends OperationPortalException {

    public ParticipantAlreadyRegisteredException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "PARTICIPANT_ALREADY_REGISTER";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format(" ({0}) has already registered in the system", this.params);
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

    @Override
    public String paramDescription() {

        return "{0} : Participant ID.";
    }

}
