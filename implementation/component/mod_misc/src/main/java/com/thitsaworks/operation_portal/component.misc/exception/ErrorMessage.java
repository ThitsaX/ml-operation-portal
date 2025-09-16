package com.thitsaworks.operation_portal.component.misc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

        if (args == null || args.length == 0) {
            return new ErrorMessage(this.code,
                                    this.defaultMessage.replaceAll("\\s*:\\s*\\[%s]", "")
                                                       .trim(),
                                    this.description);
        }

        return new ErrorMessage(this.code, String.format(this.defaultMessage, args), this.description);
    }

}


