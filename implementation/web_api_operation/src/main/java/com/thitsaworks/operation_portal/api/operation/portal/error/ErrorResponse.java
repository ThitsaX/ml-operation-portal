package com.thitsaworks.operation_portal.api.operation.portal.error;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ErrorResponse(@JsonProperty("error_code") String errorCode,
                            @JsonProperty("default_error_message") String defaultErrorMessage,
                            @JsonProperty("i18n_error_messages") Map<String, String> i18nErrorMessages) {}
