package com.thitsaworks.dfsp_portal.hubuser.exception;

import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;

import java.text.MessageFormat;

public class AlreadyAnnouncedException extends DFSPPortalException {

    public AlreadyAnnouncedException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "ALREADY_ANNOUNCED";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("The announcement already existed. ({0})", this.params);
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

    @Override
    public String paramDescription() {

        return "{0} : Announcement.";
    }

}
