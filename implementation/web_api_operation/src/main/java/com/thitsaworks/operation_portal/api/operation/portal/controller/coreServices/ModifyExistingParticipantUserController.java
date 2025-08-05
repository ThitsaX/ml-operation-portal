package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyExistingUser;
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

@RestController
@RequiredArgsConstructor
public class ModifyExistingParticipantUserController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingParticipantUserController.class);

    private final ModifyExistingUser modifyExistingUser;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/modifyParticipantUser")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws DomainException, JsonProcessingException {

        LOG.info("Modify participant user request : {}", this.objectMapper.writeValueAsString(request));

        var output = this.modifyExistingUser.execute(new ModifyExistingUser.Input(new ParticipantUserId(Long.parseLong(
                request.participantUserId())),
                                                                                  request.name(),
                                                                                  request.firstName(),
                                                                                  request.lastName(),
                                                                                  request.jobTitle(),
                                                                                  request.isActive().equalsIgnoreCase(
                                                                                          "ACTIVE") ?
                                                                                          PrincipalStatus.ACTIVE :
                                                                                          PrincipalStatus.INACTIVE));

        var response = new Response(output.participantUserId().getId().toString(), output.modified());

        LOG.info("Modify existing participant user response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @JsonProperty("participantUserId") String participantUserId,
                          @NotNull @JsonProperty("name") String name, @NotNull @JsonProperty("email") String email,
                          @NotNull @JsonProperty("firstName") String firstName,
                          @NotNull @JsonProperty("lastName") String lastName,
                          @NotNull @JsonProperty("jobTitle") String jobTitle,
                          @NotNull @JsonProperty("status") String isActive) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("participantUserId") String participantUserId,
                           @JsonProperty("modified") boolean modified) {

    }

}
