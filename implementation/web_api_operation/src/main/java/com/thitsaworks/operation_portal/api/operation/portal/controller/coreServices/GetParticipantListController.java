package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetParticipantListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantListController.class);

    private final GetParticipantList getParticipantList;

    @GetMapping(value = "/secured/getParticipantList")
    public ResponseEntity<Response> execute() throws DomainException, JsonProcessingException {

        GetParticipantList.Output output = this.getParticipantList.execute(new GetParticipantList.Input());

        List<Response.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (var participant : output.participantInfoList()) {

            participantInfoList.add(new Response.ParticipantInfo(participant.participantId()
                                                                            .getId()
                                                                            .toString(),
                                                                 participant.dfspId(),
                                                                 participant.participantName(),
                                                                 participant.description(),
                                                                 participant.logoFileType(),
                                                                 participant.logo() == null ? null : Base64.getEncoder()
                                                                                                           .encodeToString(
                                                                                                               participant.logo())));
        }

        Response response = new Response(participantInfoList);

        LOG.info("Get Participants List Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("participantInfoList") List<ParticipantInfo> participantInfoList)
        implements Serializable {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record ParticipantInfo(@JsonProperty("participantId") String participantId,
                                      @JsonProperty("dfspId") int dfspId,
                                      @JsonProperty("participantName") String participantName,
                                      @JsonProperty("description") String description,
                                      @JsonProperty("logoFileType") String logoFileType,
                                      @JsonProperty("logo") String logoBase64) implements Serializable { }

    }

}
