package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementStateList;
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
public class GetSettlementStateListController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetSettlementStateListController.class);

    private final GetSettlementStateList getSettlementStateList;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getSettlementStateList")
    public ResponseEntity<Response> execute() throws DomainException, JsonProcessingException {

        var output = this.getSettlementStateList.execute(new GetSettlementStateList.Input());

        List<Response.SettlementStateData> settlementStateDataList = new ArrayList<>();

        for (var settlementStateData : output.settlementStates()) {
            settlementStateDataList.add(new Response.SettlementStateData(settlementStateData.settlementStateId(),
                                                                         settlementStateData.enumeration()));
        }

        var response = new Response(settlementStateDataList);

        LOGGER.info("Get Settlement State Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("settlementStateList") List<SettlementStateData> settlementStateDataList) {

        public record SettlementStateData(
            @JsonProperty("settlementStateId") String settlementStateId,
            @JsonProperty("enumeration") String enumeration
        ) {

        }

    }

}

