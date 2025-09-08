package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetOtherParticipantList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetOtherParticipantListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetOtherParticipantListController.class);

    private final GetOtherParticipantList getOtherParticipantList;

    @GetMapping("/secured/getOtherParticipantList")
    public ResponseEntity<Response> execute(
        @RequestParam("participantId") String participantId) throws DomainException, JsonProcessingException {

        LOG.info("Get All Participants Request : participantId = [{}]", participantId);

        GetOtherParticipantList.Output output = this.getOtherParticipantList.execute(
            new GetOtherParticipantList.Input(new ParticipantId(Long.parseLong(participantId))));

        List<Response.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (var participant : output.participantInfoList()) {
            participantInfoList.add(new Response.ParticipantInfo(participant.participantId()
                                                                            .getId()
                                                                            .toString(),
                                                                 participant.participantName()
                                                                            .toString(),
                                                                 participant.description()));
        }

        var response = new Response(participantInfoList);

        LOG.info("Get All Participants List Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("participantInfoList") List<ParticipantInfo> participantInfoList)
        implements Serializable {

        public record ParticipantInfo(@JsonProperty("participantId") String participantId,
                                      @JsonProperty("participantName") String participantName,
                                      @JsonProperty("description") String description
        ) implements Serializable { }

    }

}
