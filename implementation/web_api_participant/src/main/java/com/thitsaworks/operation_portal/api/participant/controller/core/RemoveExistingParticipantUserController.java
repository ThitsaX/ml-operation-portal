package com.thitsaworks.operation_portal.api.participant.controller.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.usecase.participant.RemoveExistingParticipantUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class RemoveExistingParticipantUserController {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveExistingParticipantUserController.class);

    private final RemoveExistingParticipantUser removeExistingParticipantUser;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/remove_participant_user")
    public ResponseEntity<Response> execute(
            @Valid @RequestBody Request request) throws OperationPortalException, JsonProcessingException {

        LOG.info("Remove participant user request : {}", this.objectMapper.writeValueAsString(request));

        RemoveExistingParticipantUser.Output output = this.removeExistingParticipantUser.execute(
                new RemoveExistingParticipantUser.Input(new ParticipantId(Long.parseLong(request.participantId())),
                        new ParticipantUserId(Long.parseLong(request.participantUserId()))));
        var response = new Response(output.removed());

        LOG.info("Remove participant user response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull @JsonProperty("participant_id") String participantId,
            @NotNull @JsonProperty("participant_user_id") String participantUserId
    ) implements Serializable {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("removed") boolean removed
    ) implements Serializable {

    }

}
