package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GetSettlementModelController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementModelController.class);

    private final GetSettlementModel getSettlementModel;

    @GetMapping("/secured/getSettlementModel")
    public ResponseEntity<Response> execute() throws DomainException {

        var output = this.getSettlementModel.execute(new GetSettlementModel.Input());

        List<Response.SettlementModelData>
            settlementModels =
            output.settlementModels()
                  .stream()
                  .map(settlementModel -> new Response.SettlementModelData(
                      settlementModel.settlementModelId()
                                     .getEntityId()
                                     .toString(),
                      settlementModel.name(),
                      settlementModel.currencyId(),
                      settlementModel.isActive(),
                      settlementModel.autoCloseWindow(),
                      settlementModel.requireLiquidityCheck(),
                      settlementModel.autoPositionReset(),
                      settlementModel.adjustPosition(),
                      settlementModel.schedulerConfigIds()
                                     .stream()
                                     .map(SchedulerConfigId::getEntityId)
                                     .collect(Collectors.toSet())
                  ))
                  .toList();

        Response response = new Response(settlementModels);

        LOG.debug("Get all settlement models:", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(List<SettlementModelData> settlementModels) {

        public record SettlementModelData(
            @JsonProperty("settlementModelId") String settlementModelId,
            @JsonProperty("name") String name,
            @JsonProperty("currencyId") String currencyId,
            @JsonProperty("isActive") boolean isActive,
            @JsonProperty("autoCloseWindow") boolean autoCloseWindow,
            @JsonProperty("requireLiquidityCheck") boolean requireLiquidityCheck,
            @JsonProperty("autoPositionReset") boolean autoPositionReset,
            @JsonProperty("adjustPosition") boolean adjustPosition,
            @JsonProperty("schedulerConfigIds") Set<Long> schedulerConfigIds) {

        }

    }

}
