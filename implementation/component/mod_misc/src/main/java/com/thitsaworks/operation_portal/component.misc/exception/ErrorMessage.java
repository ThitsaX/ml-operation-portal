package com.thitsaworks.operation_portal.component.misc.exception;

import lombok.Getter;

import java.text.MessageFormat;
import java.util.Map;

@Getter
public class ErrorMessage {

    private final String code;

    private final String defaultMessage;

    private final String description;

    public ErrorMessage(String code, String defaultMessage, String description) {

        this.code = code;
        this.defaultMessage = defaultMessage;
        this.description = description;
    }
    public ErrorMessage(String code, String defaultMessage) {
        this(code, defaultMessage, null);
    }



    public String code() {

        return code;
    }

    public String defaultMessage() {

        return defaultMessage;
    }

    public String description() {

        return description;
    }
    public String format(Object... args) {
        return MessageFormat.format(defaultMessage, args);
    }


    public ErrorMessage withArgs(Object... args) {
        return new ErrorMessage(code, format(args), description);
    }



}


