package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.ChangeCurrentPassword;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class UpdateParticipantAmountController {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateParticipantAmountController.class);

    private final ChangeCurrentPassword changeCurrentPassword;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/secured/updateParticipantAmount")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws DomainException, JsonProcessingException {

        LOG.info("Update Participant Amount request : {}", objectMapper.writeValueAsString(request));

        UserContext userContext =
                (UserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();

       return  null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            String transferId,
            String externalReference,
            String action,
            String reason,
            Money amount
    ) implements Serializable {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("accessKey") String accessKey,
            @JsonProperty("secretKey") String secretKey
    ) implements Serializable {}

}