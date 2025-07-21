package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateNewParticipantUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class CreateNewUserController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewUserController.class);

    private final CreateNewParticipantUser createNewParticipantUser;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/secured/createNewUser")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

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
                                               request.userStatus.equalsIgnoreCase("ACTIVE") ? PrincipalStatus.ACTIVE :
                                                   PrincipalStatus.INACTIVE));

        Response response = new Response(output.created());

        LOG.info("Create new  user response: {}", objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        @NotNull @JsonProperty("name") String name,
        @NotNull @Pattern(
            regexp = Email.FORMAT,
            message = "Email must be with valid format.") @JsonProperty("email") String email,
        @NotNull @JsonProperty("password") String password,
        @NotNull @JsonProperty("firstName") String firstName,
        @NotNull @JsonProperty("lastName") String lastName,
        @NotNull @JsonProperty("jobTitle") String jobTitle,
        @NotNull @JsonProperty("participantId") String participantId,
        @NotNull @JsonProperty("userRoleType") UserRoleType userRoleType,
        @NotNull @JsonProperty("userStatus") String userStatus) implements Serializable {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("isCreated") boolean created) implements Serializable {

    }

}
