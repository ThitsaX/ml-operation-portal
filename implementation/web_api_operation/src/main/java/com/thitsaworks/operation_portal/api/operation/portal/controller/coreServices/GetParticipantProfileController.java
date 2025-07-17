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

@RestController
@RequiredArgsConstructor
public class GetParticipantProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantProfileController.class);

    private final GetParticipantProfile getParticipantProfile;

    @GetMapping(value = "/secured/getParticipantProfile")
    public ResponseEntity<Response> execute(@RequestParam("participantId") String participantId)
        throws DomainException {

        var
            output =
            this.getParticipantProfile.execute(new GetParticipantProfile.Input(new ParticipantId(Long.parseLong(
                participantId))));

        var response = new Response(output.participantId()
                                          .getEntityId()
                                          .toString(),
                                    output.dfspCode(),
                                    output.name(),
                                    output.address(),
                                    output.mobile()
                                          .getValue(),
                                    output.logo(),
                                    output.createdDate()
                                          .getEpochSecond());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("participantId") String participantId,
                           @JsonProperty("dfspCode") String dfspCode,
                           @JsonProperty("name") String name,
                           @JsonProperty("address") String address,
                           @JsonProperty("mobile") String mobile,
                           @JsonProperty("logo") byte[] logo,
                           @JsonProperty("createdDate") long createdDate) implements Serializable { }

}
