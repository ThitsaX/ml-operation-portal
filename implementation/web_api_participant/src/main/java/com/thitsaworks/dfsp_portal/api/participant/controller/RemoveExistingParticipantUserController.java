package com.thitsaworks.dfsp_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.dfsp_portal.usecase.participant.RemoveExistingParticipantUser;
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
public class RemoveExistingParticipantUserController {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveExistingParticipantUserController.class);

    @Autowired
    private RemoveExistingParticipantUser removeExistingParticipantUser;

    @RequestMapping(value = "/secured/remove_participant_user", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(
            @Valid @RequestBody Request request) throws DFSPPortalException {

        RemoveExistingParticipantUser.Output output = this.removeExistingParticipantUser.execute(
                new RemoveExistingParticipantUser.Input(new ParticipantId(Long.parseLong(request.participantId)),
                        new ParticipantUserId(Long.parseLong(request.participantUserId))));

        return new ResponseEntity<>(new Response(output.isRemoved()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NotNull
        @JsonProperty("participant_id")
        private String participantId;

        @NotNull
        @JsonProperty("participant_user_id")
        private String participantUserId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("removed")
        private boolean removed;

    }

}
