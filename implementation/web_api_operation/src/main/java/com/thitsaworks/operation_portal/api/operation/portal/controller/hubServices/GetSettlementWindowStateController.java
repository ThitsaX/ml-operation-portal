package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowStateData;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementWindowState;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetSettlementWindowStateController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementWindowStateController.class);

    private final GetSettlementWindowState getSettlementWindowState;

    @GetMapping("/secured/getSettlementWindowState")
    public ResponseEntity<Response> execute() throws DomainException {

        var output = this.getSettlementWindowState.execute(new GetSettlementWindowState.Input());

        List<Response.SettlementWindowStateData> settlementWindowStateDataList = new ArrayList<>();

        for (SettlementWindowStateData data : output.settlementWindowStates()) {
            settlementWindowStateDataList.add(new Response.SettlementWindowStateData(data.settlementWindowStateId(), data.enumeration()));
        }

        var response = new Response(settlementWindowStateDataList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response( @JsonProperty("settlementWindowStateDataList") List<SettlementWindowStateData> settlementWindowStateDataList) {

        public record SettlementWindowStateData(@JsonProperty("settlementWindowStateId") String settlementWindowStateId,
                                                @JsonProperty("enumeration") String enumeration) {

        }
    }




}
