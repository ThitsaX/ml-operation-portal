package com.thitsaworks.operation_portal.api.hub_operator.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@RequiredArgsConstructor
public class ErrorHandlerAdvice {

    private final ErrorResponseBuilder errorResponseBuilder;

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {

        return this.errorResponseBuilder.convert(exception);

    }

}
