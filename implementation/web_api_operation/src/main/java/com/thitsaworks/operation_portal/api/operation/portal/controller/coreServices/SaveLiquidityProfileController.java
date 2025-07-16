package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.core_services.SaveLiquidityProfile;
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
public class SaveLiquidityProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(SaveLiquidityProfileController.class);

    private final SaveLiquidityProfile saveLiquidityProfile;

    @PostMapping(value = "/secured/saveLiquidityProfile")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        var id = request.liquidityProfileId();
        var liquidityProfileId = id == null || id.isBlank() ? null : new LiquidityProfileId(Long.parseLong(
            id));

        var output = this.saveLiquidityProfile.execute(new SaveLiquidityProfile.Input(new ParticipantId(Long.parseLong(
            request.participantId())),
                                                                                      liquidityProfileId,
                                                                                      request.bankName(),
                                                                                      request.accountName(),
                                                                                      request.accountNumber(),
                                                                                      request.currency()));

        var response = new Response(output.saved(),
                                    output.liquidityProfileId()
                                          .getEntityId()
                                          .toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @JsonProperty("participantId") String participantId,
                          @JsonProperty("liquidityProfileId") String liquidityProfileId,
                          @NotNull @JsonProperty("bankName") String bankName,
                          @NotNull @JsonProperty("accountName") String accountName,
                          @NotNull @JsonProperty("accountNumber") String accountNumber,
                          @NotNull @JsonProperty("currency") String currency) implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("isSaved") boolean isSaved,
                           @JsonProperty("liquidityProfileId") String liquidityProfileId) implements Serializable { }

}
