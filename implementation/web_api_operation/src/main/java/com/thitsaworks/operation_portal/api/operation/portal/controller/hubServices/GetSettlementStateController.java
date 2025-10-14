package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementStateData;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementState;
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
public class GetSettlementStateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetSettlementStateController.class);

    private final GetSettlementState getSettlementState;

    @GetMapping("/secured/getSettlementState")
    public ResponseEntity<Response> execute() throws DomainException {

        var output = this.getSettlementState.execute(new GetSettlementState.Input());

        List<Response.SettlementStateData> settlementStateDataList = new ArrayList<>();

        for (SettlementStateData data : output.settlementStates()) {
            settlementStateDataList.add(new Response.SettlementStateData(data.settlementStateId(), data.enumeration()));
        }

        var response = new Response(settlementStateDataList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("settlementState") List<SettlementStateData> settlementStateDataList){

        public record SettlementStateData(
                @JsonProperty("settlementStateId") String settlementStateId,
                @JsonProperty("enumeration") String enumeration
        ){

        }

    }

}

