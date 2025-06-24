package com.thitsaworks.operation_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.hub_operator.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.usecase.hub_operator.ModifyParticipantShortName;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class ModifyParticipantShortNameController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantShortNameController.class);

    private final ModifyParticipantShortName modifyExistingParticipant;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/secured/modify_participant_shortName")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws OperationPortalException, JsonProcessingException {

        LOG.info("Modify participant shortName request: {}", objectMapper.writeValueAsString(request));

        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        ModifyParticipantShortName.Output output = this.modifyExistingParticipant.execute(
                new ModifyParticipantShortName.Input(new ParticipantId(Long.parseLong(request.participantId())),
                                                     request.companyShortName(),
                                                     userContext.accessKey()));

        Response response = new Response(output.participantId().getId().toString(),
                                         output.modified());

        LOG.info("Modify participant shortName response: {}", objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull
            @JsonProperty("participant_id")
            String participantId,

            @JsonProperty("company_short_name")
            String companyShortName) implements Serializable {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("participant_id")
            String participantId,

            @JsonProperty("modified")
            boolean modified) implements Serializable {
    }

}
