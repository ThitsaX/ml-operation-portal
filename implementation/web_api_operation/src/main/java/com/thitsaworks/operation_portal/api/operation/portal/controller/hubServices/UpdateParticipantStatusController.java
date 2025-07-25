package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices.ModifyApprovalActionController;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.common.identifier.ApprovalRequestId;
import com.thitsaworks.operation_portal.component.common.type.ApprovalActionType;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.ChangeCurrentPassword;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyApprovalAction;
import com.thitsaworks.operation_portal.usecase.operation_portal.UpdateParticipantStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class UpdateParticipantStatusController {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateParticipantStatusController.class);

    private final UpdateParticipantStatus updateParticipantStatus;

    private final ObjectMapper objectMapper;

    @PutMapping(value = "/secured/updateParticipantStatus")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws DomainException, JsonProcessingException {

        LOG.info("Update Participant Status request : {}", objectMapper.writeValueAsString(request));

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();
        var
            output =
            this.updateParticipantStatus.execute(new UpdateParticipantStatus.Input(request.participantName(),
                                                                                   request.participantCurrencyId(),
                                                                                   request.activeStatus()));

        var response = new Response(output.participantName(), output.participantCurrencyId(), output.activeStatus());

        LOG.info("Update Approval Response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        String participantName,
        int participantCurrencyId,
        String activeStatus
    ) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        String participantName,
        int participantCurrencyId,
        String activeStatus
    ) implements Serializable { }

}