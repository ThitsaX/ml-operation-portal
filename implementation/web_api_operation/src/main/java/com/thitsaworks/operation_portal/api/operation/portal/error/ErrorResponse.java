package com.thitsaworks.operation_portal.api.operation.portal.error;


import com.fasterxml.jackson.annotation.JsonProperty;



public record ErrorResponse(
        @JsonProperty("error_code") String errorCode,
        @JsonProperty("default_error_message") String defaultErrorMessage,
        @JsonProperty("description") String description
) {

}
