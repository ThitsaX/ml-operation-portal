package com.thitsaworks.operation_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.operation_portal.usecase.participant.GetExistingParticipantUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.NonNull;
import java.io.Serializable;

@RestController
public class GetExitingParticipantUserController {

    private static final Logger LOG = LoggerFactory.getLogger(GetExitingParticipantUserController.class);

    @Autowired
    private GetExistingParticipantUser getExistingParticipantUser;

    @RequestMapping(value = "/secured/get_exiting_participant_user", method = RequestMethod.GET)
    public ResponseEntity<Response> execute(@Valid @RequestParam String participantUserId) throws DFSPPortalException {

        GetExistingParticipantUser.Output output = this.getExistingParticipantUser.execute(
                new GetExistingParticipantUser.Input(new ParticipantUserId(Long.parseLong(participantUserId))));

        return new ResponseEntity<>(new Response(output.getParticipantUserId().getId().toString(),
                        output.getName(), output.getEmail().getValue(), output.getFirstName(), output.getLastName(),
                        output.getJobTitle(), output.getCreatedDate()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NonNull
        @JsonProperty("participant_user_id")
        private String participantUserId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("participant_user_id")
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

        @JsonProperty("created_date")
        private Long createdDate;

    }
}
