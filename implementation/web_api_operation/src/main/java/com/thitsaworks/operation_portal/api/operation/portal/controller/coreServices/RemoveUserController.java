package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveExistingParticipantUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class RemoveExistingParticipantUserController {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveExistingParticipantUserController.class);

    private final RemoveExistingParticipantUser removeExistingParticipantUser;

    @PostMapping("/secured/removeParticipantUser")
    public ResponseEntity<Response> execute(
        @Valid @RequestBody Request request) throws DomainException, JsonProcessingException {

        LOG.info("Remove Existing Participant User Request: [{}]", request);

        RemoveExistingParticipantUser.Output output = this.removeExistingParticipantUser.execute(
            new RemoveExistingParticipantUser.Input(new ParticipantId(Long.parseLong(request.participantId())),
                                                    new ParticipantUserId(Long.parseLong(request.participantUserId()))));
        var response = new Response(output.removed());

        LOG.info("Remove Existing Participant User Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @JsonProperty("participantId") String participantId,
                          @NotNull @JsonProperty("participantUserId") String participantUserId)
        implements Serializable {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("isRemoved") boolean isRemoved) implements Serializable { }

}
