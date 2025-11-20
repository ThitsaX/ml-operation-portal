package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateSettlementModel;
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
public class CreateSettlementModelController {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSettlementModelController.class);

    private final CreateSettlementModel createSettlementModel;

    @PostMapping("/secured/createSettlementModel")
    public ResponseEntity<Response> execute(
            @Valid @RequestBody Request request) throws DomainException, JsonProcessingException {

        LOG.info("Create New Settlement Model Request : [{}]", request);

        CreateSettlementModel.Output output = this.createSettlementModel.execute(
                new CreateSettlementModel.Input(request.name(),
                                                request.modelType(),
                                                (request.currencyId().isEmpty() || request.currencyId().isBlank()) ?
                                                        null : request.currencyId(),
                                                request.zoneId(),
                                                request.requireLiquidityCheck(),
                                                request.autoPositionReset(),
                                                request.adjustPosition()));

        var response = new Response(output.created(),
                                    output.settlementModelId()
                                          .getEntityId()
                                          .toString());

        LOG.info("Create New Settlement Model Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(@NotNull @JsonProperty("name") String name,
                          @JsonProperty("modelType") String modelType,
                          @JsonProperty("currencyId") String currencyId,
                          @JsonProperty("zoneId") String zoneId,
                          @JsonProperty("requireLiquidityCheck") boolean requireLiquidityCheck,
                          @JsonProperty("autoPositionReset") boolean autoPositionReset,
                          @JsonProperty("adjustPosition") boolean adjustPosition) implements Serializable {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("isCreated") boolean isCreated,
                           @JsonProperty("settlementModelId") String settlementModelId) implements Serializable {
    }

}
