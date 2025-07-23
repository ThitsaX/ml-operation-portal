package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateParticipantNDC;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class CreateParticipantNDCController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantNDCController.class);

    private final CreateParticipantNDC createParticipantNDC;

    @PostMapping(value = "/secured/createParticipantNDC")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
            throws JsonProcessingException, DomainException {

        var output = this.createParticipantNDC.execute(new CreateParticipantNDC.Input(request.dfspCode,
                                                                                      request.currency,
                                                                                      request.ndcPercent,
                                                                                      request.ndcAmount));

        var response = new Response(output.participantNDCId().getEntityId().toString());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank String dfspCode, @NotNull @NotBlank String currency,
                          BigDecimal ndcPercent, BigDecimal ndcAmount) implements Serializable {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(String participantNDCId) implements Serializable {}

}
