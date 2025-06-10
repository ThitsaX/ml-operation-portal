package com.thitsaworks.operation_portal.api.hub_operator.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetAllParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetAllParticipantsController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllParticipantsController.class);

    @Autowired
    private GetAllParticipant getAllParticipant;

    @RequestMapping(value = "/secured/get_all_participants", method = RequestMethod.GET)
    public ResponseEntity<Response> execute() throws DFSPPortalException {

        GetAllParticipant.Output output = this.getAllParticipant.execute(
                new GetAllParticipant.Input());

        List<Response.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (var participant : output.getParticipantInfoList()) {

            participantInfoList.add(new Response.ParticipantInfo(participant.getParticipantId().getId().toString(),
                    participant.getDfsp_code().toString(), participant.getName(),participant.getDfsp_name()));
        }

        return new ResponseEntity<>(new Response(participantInfoList), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("participant_info_list")
        private List<ParticipantInfo> participantInfoList;

        @Value
        public static class ParticipantInfo implements Serializable {

            @JsonProperty("participant_id")
            private String participantId;

            @JsonProperty("dfsp_code")
            private String dfspCode;

            @JsonProperty("dfsp_name")
            private String dfsp_name;

            @JsonProperty("company_name")
            private String name;

        }
    }
}
