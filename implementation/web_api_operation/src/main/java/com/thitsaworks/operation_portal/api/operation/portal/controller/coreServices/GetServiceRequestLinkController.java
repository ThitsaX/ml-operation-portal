package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetServiceRequestLinkController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetServiceRequestLinkController.class);

    private static final String SERVICE_REQUEST_URL = "https://thitsa.atlassian.net/servicedesk/customer/portal/11/group/33/create/10548";

    @GetMapping(value = "/secured/getServiceRequestLink")
    public ResponseEntity<Response> execute() {
        return ResponseEntity.ok(new Response(SERVICE_REQUEST_URL));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("serviceRequest") String ticketUrl
    ) {
    }
}
