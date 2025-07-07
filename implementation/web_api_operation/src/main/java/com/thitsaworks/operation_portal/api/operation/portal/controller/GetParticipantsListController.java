package com.thitsaworks.operation_portal.api.operation.portal.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetAllParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetParticipantsListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantsListController.class);

    private final GetAllParticipant getAllParticipant;

    private final ObjectMapper objectMapper;

    @GetMapping(value = "/secured/getParticipantsList")
    public ResponseEntity<Response> execute()
            throws DomainException, JsonProcessingException {

        GetAllParticipant.Output output = this.getAllParticipant.execute(
                new GetAllParticipant.Input());

        List<Response.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (var participant : output.participantInfoList()) {

            participantInfoList.add(new Response.ParticipantInfo(participant.participantId().getId().toString(),
                                                                 participant.dfsp_code(),
                                                                 participant.name(),
                                                                 participant.dfsp_name()));
        }

        Response response = new Response(participantInfoList);

        LOG.info("Get participants list response : {}", objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("participantInfoList") List<ParticipantInfo> participantInfoList)
            implements Serializable {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record ParticipantInfo(
                @JsonProperty("participantId") String participantId,
                @JsonProperty("dfspId") String dfspId,
                @JsonProperty("dfspName") String dfspName,
                @JsonProperty("companyName") String companyName) implements Serializable {
        }
    }
}
