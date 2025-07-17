package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAllOtherParticipants;
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
public class GetAllOtherParticipantsController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllOtherParticipantsController.class);

    private final GetAllOtherParticipants getAllOtherParticipants;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getAllOtherParticipants")
    public ResponseEntity<Response> execute(
            @RequestParam("participantId") String participantId) throws DomainException, JsonProcessingException {

        LOG.info("Get all participants request : participantId = {}", participantId);

        GetAllOtherParticipants.Output output = this.getAllOtherParticipants.execute(
            new GetAllOtherParticipants.Input(new ParticipantId(Long.parseLong(participantId))));

        List<Response.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (var participant : output.participantInfoList()) {
            participantInfoList.add(new Response.ParticipantInfo(participant.participantId()
                                                                            .getId()
                                                                            .toString(),
                                                                 participant.dfsp_code()
                                                                            .toString(), participant.name()));
        }

        var response = new Response(participantInfoList);

        LOG.info("Get all participants response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("participantInfoList") List<ParticipantInfo> participantInfoList
    ) implements Serializable {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record ParticipantInfo(
                @JsonProperty("participantId") String participantId,
                @JsonProperty("dfspCode") String dfspCode,
                @JsonProperty("dfspName") String name
        ) implements Serializable { }

    }

}
