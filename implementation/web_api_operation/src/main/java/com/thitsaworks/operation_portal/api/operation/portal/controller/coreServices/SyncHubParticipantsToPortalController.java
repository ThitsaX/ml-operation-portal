package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.SyncHubParticipantsToPortal;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SyncHubParticipantsToPortalController {

    private static final Logger LOG = LoggerFactory.getLogger(SyncHubParticipantsToPortalController.class);

    private final SyncHubParticipantsToPortal syncHubParticipantsToPortal;

    @PostMapping(value = "/secured/syncHubParticipantsToPortal")
    public ResponseEntity<Response> execute()
        throws DomainException, JsonProcessingException {

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        SyncHubParticipantsToPortal.Output output = this.syncHubParticipantsToPortal.execute(
            new SyncHubParticipantsToPortal.Input());

        var response = new Response(output.synced());

        LOG.info("Sync hub participants to portal response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("isSynced") boolean isSynced
    ) { }

}