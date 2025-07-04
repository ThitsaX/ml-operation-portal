package com.thitsaworks.operation_portal.api.participant.error;

import com.thitsaworks.operation_portal.api.participant.security.exception.SecurityErrors;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@Component
public class ErrorResponseBuilder {

    Map<String, String> i18nErrorMessages = new HashMap<>();

    public ResponseEntity<ErrorResponse> convert(Exception exception) {

        if (exception instanceof DomainException de) {

            i18nErrorMessages.put("en", de.getErrorMessage().description());

            var errorResponse = new ErrorResponse(de.getErrorMessage().code(),
                                                  de.getErrorMessage().description(), i18nErrorMessages);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (exception instanceof MethodArgumentNotValidException e) {

            String message = (e.getBindingResult().getFieldErrors().isEmpty()) ? null :
                    String.format("[ %s ] %s",
                                  e.getBindingResult().getFieldErrors().getFirst().getField(),
                                  e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage());

            i18nErrorMessages.put("en", message);

            var errorResponse = new ErrorResponse(SecurityErrors.MISSING_MANDATORY_ELEMENT_OR_SYNTAX.code(),
                                                  message, i18nErrorMessages);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }

        i18nErrorMessages.put("en", exception.getCause().getMessage());

        var errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR",
                                              exception.getCause().getMessage(),
                                              i18nErrorMessages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }

}