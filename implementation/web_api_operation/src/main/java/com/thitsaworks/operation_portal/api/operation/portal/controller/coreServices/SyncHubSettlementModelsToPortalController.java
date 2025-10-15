package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.SyncHubSettlementModelsToPortal;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SyncHubSettlementModelsToPortalController {

    private static final Logger LOG = LoggerFactory.getLogger(SyncHubSettlementModelsToPortalController.class);

    private final SyncHubSettlementModelsToPortal syncHubSettlementModelsToPortal;

    @PostMapping(value = "/secured/syncHubSettlementModelsToPortal")
    public ResponseEntity<Response> execute()
        throws DomainException, JsonProcessingException {

        SyncHubSettlementModelsToPortal.Output output = this.syncHubSettlementModelsToPortal.execute(
            new SyncHubSettlementModelsToPortal.Input());

        var response = new Response(output.synced());

        LOG.info("Sync Hub Settlement Models To Portal Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("isSynced") boolean isSynced
    ) { }

}