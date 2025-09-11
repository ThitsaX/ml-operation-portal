package com.thitsaworks.operation_portal.api.operation.portal.error;

import com.thitsaworks.operation_portal.api.operation.portal.security.exception.SecurityErrors;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.InputException;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class ErrorResponseBuilder {

    public ResponseEntity<ErrorResponse> convert(Exception exception) {

        String errorCode;
        String defaultErrorMessage;
        String description = null;

        if (exception instanceof HubServicesApiException e) {
            errorCode = e.getErrorInformation().getErrorCode();
            defaultErrorMessage = e.getErrorInformation().getErrorDescription();
            description = e.getErrorMessage().getDefaultMessage();

            var errorResponse = new ErrorResponse(errorCode, defaultErrorMessage, description);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (exception instanceof DomainException e) {
            errorCode = e.getErrorMessage().code();
            defaultErrorMessage = e.getErrorMessage().getDefaultMessage();
            description = e.getErrorMessage().description();
            if (description == null) {
                description = "";
            }

            var errorResponse = new ErrorResponse(errorCode, defaultErrorMessage, description);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (exception instanceof InputException e) {
            errorCode = e.getErrorMessage().code();
            defaultErrorMessage = e.getErrorMessage().getDefaultMessage();
            description = e.getErrorMessage().description();
            if (description == null) {
                description = "";
            }

            var errorResponse = new ErrorResponse(errorCode, defaultErrorMessage, description);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (exception instanceof MethodArgumentNotValidException e) {
            String message = (e.getBindingResult().getFieldErrors().isEmpty()) ? null :
                    String.format("[ %s ] %s",
                            e.getBindingResult().getFieldErrors().getFirst().getField(),
                            e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage());

            errorCode = SecurityErrors.MISSING_MANDATORY_ELEMENT_OR_SYNTAX.code();
            defaultErrorMessage = message;

            var errorResponse = new ErrorResponse(errorCode, defaultErrorMessage, description);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        errorCode = "INTERNAL_SERVER_ERROR";
        defaultErrorMessage = exception.getMessage();

        var errorResponse = new ErrorResponse(errorCode, defaultErrorMessage, description);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}

