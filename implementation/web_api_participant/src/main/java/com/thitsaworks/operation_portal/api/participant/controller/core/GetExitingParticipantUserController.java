package com.thitsaworks.operation_portal.api.participant.controller.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.usecase.participant.GetExistingParticipantUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class GetExitingParticipantUserController {

    private static final Logger LOG = LoggerFactory.getLogger(GetExitingParticipantUserController.class);

    private final GetExistingParticipantUser getExistingParticipantUser;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/get_exiting_participant_user")
    public ResponseEntity<Response> execute(@Valid @RequestParam String participantUserId)
            throws OperationPortalException, JsonProcessingException {

        LOG.info("Get participant user request : participantUserId = {}", participantUserId);
        var output = this.getExistingParticipantUser.execute(
                new GetExistingParticipantUser.Input(new ParticipantUserId(Long.parseLong(participantUserId))));
        var response = new Response(output.participantUserId().getId().toString(),
                                    output.name(),
                                    output.email().getValue(),
                                    output.firstName(),
                                    output.lastName(),
                                    output.jobTitle(),
                                    output.createdDate());
        LOG.info("Get participant user response : {}", this.objectMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull @JsonProperty("participant_user_id") String participantUserId
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("participant_user_id") String participantUserId,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName,
            @JsonProperty("job_title") String jobTitle,
            @JsonProperty("created_date") Long createdDate
    ) {}

}
