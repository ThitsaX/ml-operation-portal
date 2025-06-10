package com.thitsaworks.operation_portal.api.participant.security.exception;

import lombok.NoArgsConstructor;

import java.text.MessageFormat;

@NoArgsConstructor
public class AccountInactiveException extends AccessDeniedException {

    public AccountInactiveException(String params) {

        super(params);
    }

    @Override
    public String errorCode() {

        return "ACCOUNT_INACTIVE";
    }

    @Override
    public String defaultErrorMessage() {

        return MessageFormat.format("Account is inactive for accessKey : ({0})", this.params);
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
