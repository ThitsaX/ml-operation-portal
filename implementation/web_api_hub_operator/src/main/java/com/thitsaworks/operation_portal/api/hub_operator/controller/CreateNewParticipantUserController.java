package com.thitsaworks.operation_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.iam.domain.UserRoleType;
import com.thitsaworks.operation_portal.iam.type.PrincipalStatus;
import com.thitsaworks.operation_portal.iam.type.RealmType;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.usecase.common.CreateNewParticipantUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
public class CreateNewParticipantUserController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewParticipantUserController.class);

    @Autowired
    private CreateNewParticipantUser createNewParticipantUser;

    @RequestMapping(value = "/secured/create_new_participant_user", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DFSPPortalException {

        CreateNewParticipantUser.Output output = this.createNewParticipantUser.execute(
                new CreateNewParticipantUser.Input(request.name, new Email(request.email), request.password,
                        request.firstName, request.lastName, request.jobTitle,
                        new ParticipantId(Long.parseLong(request.participantId)), request.userRoleType,
                        RealmType.PARTICIPANT,
                        request.activeStatus ? PrincipalStatus.ACTIVE : PrincipalStatus.INACTIVE));

        return new ResponseEntity<>(new Response(output.isCreated()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NonNull
        @JsonProperty("name")
        private String name;

        @NonNull
        @Pattern(regexp = Email.FORMAT, message = "Email must be with valid format.")
        @JsonProperty("email")
        private String email;

        @NonNull
        @JsonProperty("password")
        private String password;

        @NonNull
        @JsonProperty("first_name")
        private String firstName;

        @NonNull
        @JsonProperty("last_name")
        private String lastName;

        @NonNull
        @JsonProperty("job_title")
        private String jobTitle;

        @NonNull
        @JsonProperty("participant_id")
        private String participantId;

        @NonNull
        @JsonProperty("user_role_type")
        private UserRoleType userRoleType;

        @NonNull
        @JsonProperty("is_active")
        private boolean activeStatus;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("is_created")
        private boolean created;

    }
}
