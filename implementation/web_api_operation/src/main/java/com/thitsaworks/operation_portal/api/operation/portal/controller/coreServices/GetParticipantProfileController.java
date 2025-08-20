package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantProfile;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
public class GetParticipantProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantProfileController.class);

    private final GetParticipantProfile getParticipantProfile;

    @GetMapping(value = "/secured/getParticipantProfile")
    public ResponseEntity<Response> execute(@RequestParam("participantId") String participantId)
        throws DomainException {

        LOG.info("Get Participant Profile Request: ParticipantId = [{}]", participantId);

        var
            output =
            this.getParticipantProfile.execute(new GetParticipantProfile.Input(new ParticipantId(Long.parseLong(
                participantId))));

        var response = new Response(output.participantId()
                                          .getEntityId()
                                          .toString(),
                                    output.participantName(),
                                    output.description(),
                                    output.address(),
                                    output.mobile() != null ? output.mobile().getValue() : null,
                                    output.logoDataType(),
                                    output.logoBase64() == null ? null : Base64.getEncoder()
                                                                               .encodeToString(output.logoBase64()),
                                    output.createdDate()
                                          .getEpochSecond());

        LOG.info("Get Participant Profile Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("participantId") String participantId,
                           @JsonProperty("participantName") String participantName,
                           @JsonProperty("description") String description,
                           @JsonProperty("address") String address,
                           @JsonProperty("mobile") String mobile,
                           @JsonProperty("logoFileType") String logoFileType,
                           @JsonProperty("logo") String logoBase64,
                           @JsonProperty("createdDate") long createdDate) implements Serializable { }

}
