package com.thitsaworks.operation_portal.api.participant.controller.reporting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.hub_services.GetSettlementId;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetSettlementIdController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementIdController.class);

    private final GetSettlementId getSettlementId;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/get_settlement_id")
    public ResponseEntity<Response> execute(
            @RequestParam("start_date") String startDate,
            @RequestParam("end_date") String endDate,
            @RequestParam("timezoneOffset") String timezoneOffset)
            throws DomainException, JsonProcessingException {

        LOG.info("Get settlement id filters : startDate = {}, endDate = {}, timezoneOffset ={}",
                startDate, endDate, timezoneOffset);

        GetSettlementId.Output output = this.getSettlementId.execute(
                new GetSettlementId.Input(Instant.parse(startDate), Instant.parse(endDate), timezoneOffset));

        List<SettlementIdInfo> settlementIdInfoList = output.settlementIds().stream()
                                                            .map(idType -> new SettlementIdInfo(
                                                                    idType.getSettlementId()))
                                                            .toList();
        var response = new Response(settlementIdInfoList);

        LOG.info("Get settlement id response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("settlement_id_list") List<SettlementIdInfo> settlementIdDataList
    ) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SettlementIdInfo(
            @JsonProperty("settlement_id") String SettlementId
    ) {

    }

}
