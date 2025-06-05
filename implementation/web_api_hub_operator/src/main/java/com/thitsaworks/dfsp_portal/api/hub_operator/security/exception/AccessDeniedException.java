package com.thitsaworks.dfsp_portal.api.hub_operator.security.exception;

import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;

@NoArgsConstructor
public class AccessDeniedException extends DFSPPortalException {

    public AccessDeniedException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "ACCESS_DENIED";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("Account is denied for accessKey : ({0})", this.params);
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

}
