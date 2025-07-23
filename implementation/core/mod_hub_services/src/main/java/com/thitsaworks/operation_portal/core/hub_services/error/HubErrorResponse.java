package com.thitsaworks.operation_portal.core.hub_services.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record HubErrorResponse(String errorCode, String errorDescription) {

}
