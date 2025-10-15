package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementParticipant;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindow;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindowId;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateSettlement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CreateSettlementController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSettlementController.class);

    private final CreateSettlement createSettlement;

    @PostMapping(value = "/secured/createSettlement")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws DomainException, JsonProcessingException {

        LOG.info("Create Settlement Request : [{}]", request);

        UserContext userContext =
                (UserContext) SecurityContextHolder.getContext()
                                                   .getAuthentication()
                                                   .getDetails();

        var output = this.createSettlement.execute(new CreateSettlement.Input(request.settlementModel,
                                                                              request.reason,
                                                                              request.settlementWindowIdList));

        var response = new Response(output.settlementId(),
                                    output.state(),
                                    output.settlementWindowList(),
                                    output.settlementParticipantList());

        LOG.info("Create Settlement Response : {}", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            String settlementModel,
            String reason,
            List<SettlementWindowId> settlementWindowIdList
    ) implements Serializable {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            Integer settlementId,
            String state,
            List<SettlementWindow> settlementWindowList,
            List<SettlementParticipant> participantList
    ) implements Serializable {}

}