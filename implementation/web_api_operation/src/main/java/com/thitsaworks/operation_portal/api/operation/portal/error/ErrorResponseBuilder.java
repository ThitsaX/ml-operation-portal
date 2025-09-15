package com.thitsaworks.operation_portal.api.operation.portal.error;

import com.thitsaworks.operation_portal.api.operation.portal.security.exception.SecurityErrors;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import com.thitsaworks.operation_portal.component.misc.exception.InputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class ErrorResponseBuilder {

    public ResponseEntity<ErrorResponse> convert(Exception exception) {

        if (exception instanceof DomainException e) {
            ErrorMessage errorMessage = e.getErrorMessage();

            var errorResponse = new ErrorResponse(errorMessage.getCode(),
                                                  errorMessage.getDefaultMessage(),
                                                  errorMessage.getDescription());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (exception instanceof InputException e) {
            ErrorMessage errorMessage = e.getErrorMessage();

            var errorResponse = new ErrorResponse(errorMessage.getCode(),
                                                  errorMessage.getDefaultMessage(),
                                                  errorMessage.getDescription());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (exception instanceof MethodArgumentNotValidException e) {
            String message = (e.getBindingResult().getFieldErrors().isEmpty()) ? null :
                    String.format("[ %s ] %s",
                            e.getBindingResult().getFieldErrors().getFirst().getField(),
                            e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage());

            var errorResponse = new ErrorResponse(SecurityErrors.MISSING_MANDATORY_ELEMENT_OR_SYNTAX.getCode(),
                                                  message,
                                                  "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        var errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", exception.getMessage(), "");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}

