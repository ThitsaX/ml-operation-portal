package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementWindowStateList;
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
public class GetSettlementWindowStateListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementWindowStateListController.class);

    private final GetSettlementWindowStateList getSettlementWindowStateList;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getSettlementWindowStateList")
    public ResponseEntity<Response> execute() throws DomainException, JsonProcessingException {

        var output = this.getSettlementWindowStateList.execute(new GetSettlementWindowStateList.Input());

        List<Response.SettlementWindowStateData> settlementWindowStateDataList = new ArrayList<>();

        for (var settlementWindowStateData : output.settlementWindowStates()) {
            settlementWindowStateDataList.add(new Response.SettlementWindowStateData(settlementWindowStateData.settlementWindowId(),
                                                                                     settlementWindowStateData.enumeration()));
        }

        var response = new Response(settlementWindowStateDataList);

        LOG.info("Get Settlement Window State Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("settlementWindowStateList") List<SettlementWindowStateData> settlementWindowStateDataList) {

        public record SettlementWindowStateData(@JsonProperty("settlementWindowStateId") String settlementWindowStateId,
                                                @JsonProperty("enumeration") String enumeration) {

        }

    }

}
