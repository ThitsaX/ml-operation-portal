package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementModelList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GetSettlementModelListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementModelListController.class);

    private final GetSettlementModelList getSettlementModelList;

    @GetMapping("/secured/getSettlementModelList")
    public ResponseEntity<Response> execute() throws DomainException {

        var output = this.getSettlementModelList.execute(new GetSettlementModelList.Input());

        List<Response.SettlementModelData>
            settlementModels =
            output.settlementModels()
                  .stream()
                  .map(settlementModel -> new Response.SettlementModelData(
                      settlementModel.settlementModelId()
                                     .getEntityId()
                                     .toString(),
                      settlementModel.name(),
                      settlementModel.type(),
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

        LOG.info("Get All Settlement Model List: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(List<SettlementModelData> settlementModels) implements Serializable {

        public record SettlementModelData(
            @JsonProperty("settlementModelId") String settlementModelId,
            @JsonProperty("name") String name,
            @JsonProperty("type") String type,
            @JsonProperty("currencyId") String currencyId,
            @JsonProperty("isActive") boolean isActive,
            @JsonProperty("autoCloseWindow") boolean autoCloseWindow,
            @JsonProperty("requireLiquidityCheck") boolean requireLiquidityCheck,
            @JsonProperty("autoPositionReset") boolean autoPositionReset,
            @JsonProperty("adjustPosition") boolean adjustPosition,
            @JsonProperty("schedulerConfigIds") Set<Long> schedulerConfigIds) implements Serializable { }

    }

}
