package com.thitsaworks.operation_portal.component.misc.spring;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.base.CaseFormat;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class DefaultSpringRestErrorResponseBuilder
    implements SpringRestServiceExceptionHandler.SpringRestErrorResponseBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSpringRestErrorResponseBuilder.class);

    @Override
    public SpringRestServiceExceptionHandler.SpringRestErrorResponse build(Exception exception) {

        LOG.error("Error : ", exception);

        Map<String, String> i18nErrorMessages = new HashMap<>();

        if ((exception instanceof MethodArgumentNotValidException)) {

            MethodArgumentNotValidException methodArgumentNotValidException =
                (MethodArgumentNotValidException) exception;
            StringJoiner stringJoiner = new StringJoiner(",");

            List<FieldError> fieldErrors = methodArgumentNotValidException.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                stringJoiner.add(
                    "[ (" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldError.getField()) + ") : " +
                        fieldError.getDefaultMessage() + " ]");
            }

            String errorMessage = "Input error : " + stringJoiner.toString();

            i18nErrorMessages.put("en", errorMessage);

            return new SpringRestServiceExceptionHandler.SpringRestErrorResponse("INPUT_EXCEPTION",
                                                                                 errorMessage,
                                                                                 i18nErrorMessages);

        } else if (exception instanceof HttpMessageNotReadableException) {

            InvalidFormatException invalidFormatException = (InvalidFormatException) exception.getCause();

            i18nErrorMessages.put("en", invalidFormatException.getMessage());

            return new SpringRestServiceExceptionHandler.SpringRestErrorResponse("INVALID_FORMAT",
                                                                                 exception.getMessage(),
                                                                                 i18nErrorMessages);

        } else if (exception instanceof OperationPortalException) {

            OperationPortalException OperationPortalException = (OperationPortalException) exception;

            i18nErrorMessages.put("en", OperationPortalException.getMessage());

            return new SpringRestServiceExceptionHandler.SpringRestErrorResponse(
                OperationPortalException.errorCode(),
                OperationPortalException.defaultErrorMessage(), i18nErrorMessages);

        } else {

            i18nErrorMessages.put("en",
                                  exception.getCause()
                                           .getMessage());

            return new SpringRestServiceExceptionHandler.SpringRestErrorResponse("INTERNAL_SERVER_ERROR",
                                                                                 exception.getCause()
                                                                                          .getMessage(),
                                                                                 i18nErrorMessages);
        }
    }

}