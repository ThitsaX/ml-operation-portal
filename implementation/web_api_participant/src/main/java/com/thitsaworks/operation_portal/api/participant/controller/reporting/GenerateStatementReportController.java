package com.thitsaworks.operation_portal.api.participant.controller.reporting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.usecase.central_ledger.GenerateStatementReport;
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
public class GenerateStatementReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateStatementReportController.class);

    private final GenerateStatementReport generateStatementReport;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/generate_statement_report")
    public ResponseEntity<Response> execute(
            @RequestParam("start_date") String startDate, @RequestParam("end_date") String endDate,
            @RequestParam("fsp_id") String fspId,
            @RequestParam("file_type") String fileType,
            @RequestParam("timezoneoffset") String timeZoneOffset,
            @RequestParam("currencyId") String currencyId) throws OperationPortalException, JsonProcessingException {

        LOG.info(
                "Statement report filters : startDate = {0}, endDate = {1}, fspId ={2}, fileType = {3}, timeZoneOffset = {4}, currencyId = {5}",
                startDate,
                endDate,
                fspId,
                fileType,
                timeZoneOffset,
                currencyId);

        GenerateStatementReport.Output output = this.generateStatementReport.execute(
                new GenerateStatementReport.Input(Instant.parse(startDate), Instant.parse(endDate), fspId, fileType,timeZoneOffset,currencyId));

        var response = new Response(output.statementData());

        LOG.info("Statement report response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("detail_report_byte") byte[] statementReportByte) {}

}
