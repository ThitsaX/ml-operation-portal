package com.thitsaworks.operation_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.api.hub_operator.security.UserContext;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.usecase.hub_operator.ModifyParticipantShortName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.NonNull;
import java.io.Serializable;

@RestController
public class ModifyParticipantShortNameController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantShortNameController.class);

    @Autowired
    private ModifyParticipantShortName modifyExistingParticipant;

    @RequestMapping(value = "/secured/modify_participant_shortName", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DFSPPortalException {

        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

        ModifyParticipantShortName.Output output = this.modifyExistingParticipant.execute(
                new ModifyParticipantShortName.Input(new ParticipantId(Long.parseLong(request.participantId)),
                        request.companyShortName,
                        userContext.getAccessKey()));

        return new ResponseEntity<>(new Response(output.getParticipantId().getId().toString(),
                        output.isModified()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Request implements Serializable {

        @NonNull
        @JsonProperty("participant_id")
        private String participantId;

        @JsonProperty("company_short_name")
        private String companyShortName;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("participant_id")
        private String participantId;

        @JsonProperty("modified")
        private boolean modified;

    }
}
