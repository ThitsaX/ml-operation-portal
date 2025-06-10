package com.thitsaworks.operation_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.usecase.participant.GetAllOtherParticipants;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetAllOtherParticipantsController {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllOtherParticipantsController.class);

    @Autowired
    private GetAllOtherParticipants getAllOtherParticipants;

    @RequestMapping(value = "/secured/get_all_other_participants", method = RequestMethod.GET)
    public ResponseEntity<GetAllOtherParticipantsController.Response> execute(
            @RequestParam("participant_id") String participantId) throws DFSPPortalException {

        GetAllOtherParticipants.Output output = this.getAllOtherParticipants.execute(
                new GetAllOtherParticipants.Input(new ParticipantId(Long.parseLong(participantId))));

        List<Response.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (var participant : output.getParticipantInfoList()) {

            participantInfoList.add(new Response.ParticipantInfo(participant.getParticipantId().getId().toString(),
                    participant.getDfsp_code().toString(), participant.getName()));
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
            private String name;


        }

    }

}
