package com.thitsaworks.operation_portal.component.misc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.MessageFormat;

@Getter
@AllArgsConstructor
public class ErrorMessage {

    private final String code;

    private final String defaultMessage;

    private final String description;

    public ErrorMessage(String code, String defaultMessage) {

        this(code, defaultMessage, "");
    }

    public ErrorMessage code(String code) {

        return new ErrorMessage(code, defaultMessage, description);
    }

    public ErrorMessage defaultMessage(String message) {

        return new ErrorMessage(code, message, description);
    }

    public ErrorMessage description(String description) {

        return new ErrorMessage(code, defaultMessage, description);
    }

    public ErrorMessage format(Object... args) {

        return new ErrorMessage(this.code, MessageFormat.format(this.defaultMessage, args), this.description);
    }

}


