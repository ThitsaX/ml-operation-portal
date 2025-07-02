package com.thitsaworks.operation_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.usecase.common.CreateNewParticipantUser;
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
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class CreateNewParticipantUserController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewParticipantUserController.class);

    private final CreateNewParticipantUser createNewParticipantUser;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/secured/create_new_participant_user")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws OperationPortalException, JsonProcessingException {

        LOG.info("Create new participant user request: {}", objectMapper.writeValueAsString(request));

        CreateNewParticipantUser.Output output = this.createNewParticipantUser.execute(
                new CreateNewParticipantUser.Input(request.name,
                                                   new Email(request.email),
                                                   request.password,
                                                   request.firstName,
                                                   request.lastName,
                                                   request.jobTitle,
                                                   new ParticipantId(Long.parseLong(request.participantId)),
                                                   request.userRoleType,
                                                   RealmType.PARTICIPANT,
                                                   request.activeStatus ? PrincipalStatus.ACTIVE :
                                                           PrincipalStatus.INACTIVE));

        Response response = new Response(output.created());

        LOG.info("Create new participant user response: {}", objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            @NotNull @JsonProperty("name") String name,
            @NotNull @Pattern(regexp = Email.FORMAT, message = "Email must be with valid format.") @JsonProperty("email") String email,
            @NotNull @JsonProperty("password") String password,
            @NotNull @JsonProperty("first_name") String firstName,
            @NotNull @JsonProperty("last_name") String lastName,
            @NotNull @JsonProperty("job_title") String jobTitle,
            @NotNull @JsonProperty("participant_id") String participantId,
            @NotNull @JsonProperty("user_role_type") UserRoleType userRoleType,
            @NotNull @JsonProperty("is_active") boolean activeStatus) implements Serializable {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("is_created") boolean created) implements Serializable {

    }

}
