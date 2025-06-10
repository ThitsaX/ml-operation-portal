package com.thitsaworks.operation_portal.api.hub_operator.security.exception;

import lombok.NoArgsConstructor;

import java.text.MessageFormat;

@NoArgsConstructor
public class InvalidAccessKeyException extends AccessDeniedException {

    public InvalidAccessKeyException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "INVALID_ACCESS_KEY";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("Account is denied for accessKey : ({0})", this.params);
    }

    @Override
    public boolean requireTranslation() {

        return true;
    }

    @Override
    public String paramDescription() {

        return "{0} : Access Key";
    }

}
