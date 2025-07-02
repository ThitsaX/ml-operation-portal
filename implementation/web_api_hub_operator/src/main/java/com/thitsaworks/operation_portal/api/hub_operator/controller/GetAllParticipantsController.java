package com.thitsaworks.operation_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
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
public class GetAllParticipantsController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllParticipantsController.class);

    private final GetAllParticipant getAllParticipant;

    private final ObjectMapper objectMapper;

    @GetMapping(value = "/secured/get_all_participants")
    public ResponseEntity<Response> execute()
            throws OperationPortalException, JsonProcessingException {

        LOG.info("Get all participants request : {}", "");

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

        LOG.info("Get all participants response : {}", objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("participant_info_list") List<ParticipantInfo> participantInfoList)
            implements Serializable {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record ParticipantInfo(
                @JsonProperty("participant_id") String participantId,
                @JsonProperty("dfsp_code") String dfspCode,
                @JsonProperty("dfsp_name") String dfsp_name,
                @JsonProperty("company_name") String name) implements Serializable {
        }
    }
}
