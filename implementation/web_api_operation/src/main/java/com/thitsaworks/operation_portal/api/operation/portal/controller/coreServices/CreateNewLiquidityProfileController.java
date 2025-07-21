package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateNewLiquidityProfile;
import jakarta.validation.Valid;
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

@RestController
@RequiredArgsConstructor
public class CreateNewLiquidityProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewLiquidityProfileController.class);

    private final CreateNewLiquidityProfile createNewLiquidityProfile;

    @PostMapping(value = "/secured/createLiquidityProfile")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request)
        throws JsonProcessingException, DomainException {

        CreateNewLiquidityProfile.Output output = this.createNewLiquidityProfile.execute(
            new CreateNewLiquidityProfile.Input(new ParticipantId(Long.parseLong(request.participantId())),
                                                request.bankName(),
                                                request.accountName(),
                                                request.accountNumber(),
                                                request.currency()));

        Response response = new Response(output.created(),
                                         output.liquidityProfileId()
                                               .getEntityId()
                                               .toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        @NotNull @JsonProperty("participantId") String participantId,
        @NotNull @JsonProperty("bankName") String bankName,
        @NotNull @JsonProperty("accountName") String accountName,
        @NotNull @JsonProperty("accountNumber") String accountNumber,
        @NotNull @JsonProperty("currency") String currency) implements Serializable {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("isCreated") boolean created,
                           @JsonProperty("liquidityProfileId") String liquidityProfileId) implements Serializable { }

}
