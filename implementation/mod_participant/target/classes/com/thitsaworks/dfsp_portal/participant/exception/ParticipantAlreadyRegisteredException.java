package com.thitsaworks.dfsp_portal.participant.exception;

import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;

import java.text.MessageFormat;

public class ParticipantAlreadyRegisteredException extends DFSPPortalException {

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
