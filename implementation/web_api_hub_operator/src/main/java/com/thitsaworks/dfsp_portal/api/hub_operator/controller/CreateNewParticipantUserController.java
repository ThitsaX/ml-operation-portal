package com.thitsaworks.dfsp_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.iam.domain.UserRoleType;
import com.thitsaworks.dfsp_portal.iam.type.PrincipalStatus;
import com.thitsaworks.dfsp_portal.iam.type.RealmType;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.usecase.common.CreateNewParticipantUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

        @NotNull
        @JsonProperty("name")
        private String name;

        @NotNull
        @Pattern(regexp = Email.FORMAT, message = "Email must be with valid format.")
        @JsonProperty("email")
        private String email;

        @NotNull
        @JsonProperty("password")
        private String password;

        @NotNull
        @JsonProperty("first_name")
        private String firstName;

        @NotNull
        @JsonProperty("last_name")
        private String lastName;

        @NotNull
        @JsonProperty("job_title")
        private String jobTitle;

        @NotNull
        @JsonProperty("participant_id")
        private String participantId;

        @NotNull
        @JsonProperty("user_role_type")
        private UserRoleType userRoleType;

        @NotNull
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
