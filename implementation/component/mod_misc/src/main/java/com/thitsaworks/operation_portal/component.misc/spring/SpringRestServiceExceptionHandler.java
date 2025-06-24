package com.thitsaworks.operation_portal.component.misc.spring;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;
import java.util.Map;

@ControllerAdvice
public class SpringRestServiceExceptionHandler {

    @Autowired
    private SpringRestErrorResponseBuilder errorResponseBuilder;

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public SpringRestErrorResponse handle(Exception e) {

        return this.errorResponseBuilder.build(e);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpringRestErrorResponse implements Serializable {

        @JsonProperty("error_code")
        private String errorCode;

        @JsonProperty("default_error_message")
        private String defaultErrorMessage;

        @JsonProperty("i18n_error_messages")
        private Map<String, String> i18nErrorMessages;

    }

    public interface SpringRestErrorResponseBuilder {

        SpringRestErrorResponse build(Exception exception);

    }

}
