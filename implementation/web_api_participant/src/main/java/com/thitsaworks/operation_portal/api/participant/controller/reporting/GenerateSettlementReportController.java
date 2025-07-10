package com.thitsaworks.operation_portal.api.participant.controller.reporting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.hub_services.GenerateSettlementReport;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequiredArgsConstructor
public class GenerateSettlementReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementReportController.class);

    private final GenerateSettlementReport generateSettlementReport;
    private final ObjectMapper objectMapper;

    @PostMapping("/secured/generate_settlement_report")
    public ResponseEntity<Response> execute
            (@RequestParam("fsp_id") String fspId,
             @RequestParam("settlement_id") String settlementId,
             @RequestParam("file_type") String fileType,
             @RequestParam("timezoneOffset") String timezoneOffset)
            throws DomainException, JsonProcessingException {

        LOG.info(
                "Settlement report filters : settlementId = {}, fspId = {}, fileType ={}, timezoneOffset = {}",
                settlementId,
                fspId,
                fileType,
                timezoneOffset);

        GenerateSettlementReport.Output output = this.generateSettlementReport.execute(
                new GenerateSettlementReport.Input(fspId, settlementId, fileType, timezoneOffset));

        var response = new Response(output.settlementByte());

        LOG.info("Settlement report response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    // Request and Response records
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("generated") byte[] settlementByte) implements Serializable {}

}
