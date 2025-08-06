package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetUserController {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserController.class);

    private final GetUser getUser;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getUser")
    public ResponseEntity<Response> execute(@Valid @RequestParam String participantUserId)
        throws DomainException, JsonProcessingException {

        LOG.info("Get user request : participantUserId = {}", participantUserId);

        var output = this.getUser.execute(
                new GetUser.Input(new UserId(Long.parseLong(participantUserId))));

        var response = new Response(output.userId()
                                          .getId()
                                          .toString(),
                                    output.name(),
                                    output.email()
                                          .getValue(),
                                    output.firstName(),
                                    output.lastName(),
                                    output.jobTitle(),
                                    output.createdDate());
        LOG.info("Get user response : {}", this.objectMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull @JsonProperty("participantUserId") String participantUserId
    ) { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("participantUserId") String participantUserId,
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("jobTitle") String jobTitle,
            @JsonProperty("createdDate") Long createdDate
    ) { }

}
