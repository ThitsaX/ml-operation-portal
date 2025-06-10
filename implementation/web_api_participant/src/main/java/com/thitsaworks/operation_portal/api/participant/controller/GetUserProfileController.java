package com.thitsaworks.operation_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.api.participant.security.UserContext;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.operation_portal.usecase.participant.GetUserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
public class GetUserProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserProfileController.class);

    @Autowired
    private GetUserProfile getUserProfile;

    @RequestMapping(value = "/secured/get_user_profile", method = RequestMethod.GET)
    public ResponseEntity<Response> execute() throws DFSPPortalException {

        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        GetUserProfile.Output output = this.getUserProfile.execute(
                new GetUserProfile.Input(new ParticipantUserId(userContext.getParticipantUserId().getId())));

        return new ResponseEntity<>(
                new Response(output.getParticipantUserId().getId().toString(),
                        output.getName(),
                        output.getEmail().getValue(), output.getFirstName(), output.getLastName(), output.getJobTitle(),
                        output.getDfspCode(),output.getDfspName(),output.getRoleType(),output.getParticipantId().getId().toString(),
                        output.getCreatedDate()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("user_id")
        private String participantUserId;

        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        private String email;

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        @JsonProperty("job_title")
        private String jobTitle;

        @JsonProperty("dfsp_code")
        private String dfspCode;

        @JsonProperty("dfsp_name")
        private String dfspName;

        @JsonProperty("user_role_type")
        private String roleType;

        @JsonProperty("participant_id")
        private String participantId;

        @JsonProperty("created_date")
        private Long createdDate;

    }

}
