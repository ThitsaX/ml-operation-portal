package com.thitsaworks.operation_portal.api.participant.security.exception;

import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;

@NoArgsConstructor
public class AccessDeniedException extends OperationPortalException {

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
