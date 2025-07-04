package com.thitsaworks.operation_portal.api.participant.controller.reporting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.central_ledger.GenerateFeeSettlementReport;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class GenerateFeeSettlementReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateFeeSettlementReportController.class);

    private final GenerateFeeSettlementReport generateFeeSettlementReport;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/generate_fee_report")
    public ResponseEntity<Response> execute(
            @RequestParam("start_date") String startDate, @RequestParam("end_date") String endDate,
            @RequestParam("from_fsp_id") String fromFspId, @RequestParam("to_fsp_id") String toFspId,
            @RequestParam("currency") String currency,
            @RequestParam("time_zone_offset") String timezoneoffset,
            @RequestParam("file_type") String fileType) throws DomainException, JsonProcessingException {

        LOG.info(
                "Fee settlement report filters : startDate = {}, endDate = {}, fromFspId ={}, toFspId = {}, currency = {}, timezoneoffset = {}, fileType = {}",
                startDate,
                endDate,
                fromFspId,
                toFspId,
                currency,
                timezoneoffset,
                fileType);

        GenerateFeeSettlementReport.Output output = this.generateFeeSettlementReport.execute(
                new GenerateFeeSettlementReport.Input(Instant.parse(startDate), Instant.parse(endDate), fromFspId,
                                                      toFspId,
                                                      currency,
                                                      timezoneoffset,
                                                      fileType));

        var response = new Response(output.rptData());

        LOG.info("Fee settlement report response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("rpt_byte") byte[] feeReportByte) {
    }

}
