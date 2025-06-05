package com.thitsaworks.dfsp_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.iam.domain.UserRoleType;
import com.thitsaworks.dfsp_portal.iam.type.PrincipalStatus;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.dfsp_portal.usecase.participant.ModifyExistingParticipantUser;
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
import java.io.Serializable;

@RestController
public class ModifyExistingParticipantUserController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingParticipantUserController.class);

    @Autowired
    private ModifyExistingParticipantUser modifyExistingParticipantUser;

    @RequestMapping(value = "/secured/modify_participant_user", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(
            @Valid @RequestBody Request request) throws DFSPPortalException {

        ModifyExistingParticipantUser.Output output = this.modifyExistingParticipantUser.execute(
                new ModifyExistingParticipantUser.Input(
                        new ParticipantUserId(Long.parseLong(request.participantUserId)), request.name,
                        new Email(request.email), request.firstName, request.lastName, request.jobTitle,request.userRoleType,
                        request.isActive.equalsIgnoreCase("ACTIVE") ? PrincipalStatus.ACTIVE : PrincipalStatus.INACTIVE));

        return new ResponseEntity<>(new Response(output.getParticipantUserId().getId().toString(),
                        output.isModified()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NotNull
        @JsonProperty("participant_user_id")
        private String participantUserId;

        @NotNull
        @JsonProperty("name")
        private String name;

        @NotNull
        @JsonProperty("email")
        private String email;

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
        @JsonProperty("user_role_type")
        private UserRoleType userRoleType;

        @NotNull
        @JsonProperty("status")
        private String isActive;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("participant_user_id")
        private String participantUserId;

        @JsonProperty("modified")
        private boolean modified;

    }

}
