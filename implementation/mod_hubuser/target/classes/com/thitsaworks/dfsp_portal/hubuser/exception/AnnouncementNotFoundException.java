package com.thitsaworks.dfsp_portal.hubuser.exception;

import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;

import java.text.MessageFormat;

public class AnnouncementNotFoundException extends DFSPPortalException {

    public AnnouncementNotFoundException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "ANNOUNCEMENT_NOT_FOUND";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("System cannot find the announcement with ID : ({0})", this.params);
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

    @Override
    public String paramDescription() {

        return "{0} : Announcement ID.";
    }

}
