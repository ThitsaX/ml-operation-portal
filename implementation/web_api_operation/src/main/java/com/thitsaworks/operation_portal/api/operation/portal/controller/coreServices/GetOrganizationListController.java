package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantListByUserId;
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
public class GetOrganizationListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetOrganizationListController.class);

    private final GetParticipantListByUserId getParticipantListByUserId;

    @GetMapping(value = "/secured/getParticipantListByUserId")
    public ResponseEntity<Response> execute() throws DomainException {

        LOG.info("Get Participant List By User Id Request : [{}]", "");

        var input = new GetParticipantListByUserId.Input();
        var output = this.getParticipantListByUserId.execute(input);

        List<Response.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (var participantInfo : output.participantInfoList()) {
            participantInfoList.add(new Response.ParticipantInfo(participantInfo.participantId()
                                                                                .toString(),
                                                                 participantInfo.participantName(),
                                                                 participantInfo.participantDescription()));
        }

        var response = new Response(participantInfoList);

        LOG.info("Get Participant List By User Id Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(List<ParticipantInfo> participantInfoList) {

        public record ParticipantInfo(String participantId,
                                      String participantName,
                                      String participantDescription) implements Serializable { }

    }

}


