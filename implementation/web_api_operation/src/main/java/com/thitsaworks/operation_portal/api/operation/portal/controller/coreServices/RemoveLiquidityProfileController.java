package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveLiquidityProfile;
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

@RestController
@RequiredArgsConstructor
public class RemoveLiquidityProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveLiquidityProfileController.class);

    private final RemoveLiquidityProfile removeLiquidityProfile;

    @PostMapping(value = "/secured/removeLiquidityProfile")
    public ResponseEntity<Response> execute(@Valid @RequestBody Request request) throws DomainException {

        LOG.info("Remove Liquidity Profile Request : [{}]", request);

        var
            output =
            this.removeLiquidityProfile.execute(new RemoveLiquidityProfile.Input(new ParticipantId(Long.parseLong(
                request.participantId())),
                                                                                 new LiquidityProfileId(Long.parseLong(
                                                                                     request.liquidityProfileId()))));

        var response = new Response(output.removed());

        LOG.info("Remove Liquidity Profile Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @NotBlank @JsonProperty("participantId") String participantId,
                          @NotNull @NotBlank @JsonProperty("liquidityProfileId") String liquidityProfileId)
        implements Serializable { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("isRemoved") boolean isRemoved) implements Serializable { }

}
