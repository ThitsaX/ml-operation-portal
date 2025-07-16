package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.usecase.core_services.ModifyParticipantProfile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ModifyParticipantProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantProfileController.class);

    private final ModifyParticipantProfile modifyParticipantProfile;

    @PostMapping("/secured/modifyParticipant")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        UserContext
            userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        ModifyParticipantProfile.Output output = this.modifyParticipantProfile.execute(
            new ModifyParticipantProfile.Input(new ParticipantId(Long.parseLong(request.participantId())),
                                               request.companyName(),
                                               request.address(),
                                               new Mobile(request.mobile()),
                                               request.logoType(),
                                               request.logo(),
                                               userContext.accessKey()));

        var response = new Response(output.participantId()
                                          .getId()
                                          .toString(), output.modified());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        @NotNull @JsonProperty("participantId") String participantId,
        @NotNull @JsonProperty("name") String companyName,
        @NotNull @JsonProperty("address") String address,
        @NotNull @JsonProperty("mobile") String mobile,
        @JsonProperty("logoType") String logoType,
        @JsonProperty("logo") byte[] logo
    ) { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("participantId") String participantId,
        @JsonProperty("modified") boolean modified
    ) { }

}
