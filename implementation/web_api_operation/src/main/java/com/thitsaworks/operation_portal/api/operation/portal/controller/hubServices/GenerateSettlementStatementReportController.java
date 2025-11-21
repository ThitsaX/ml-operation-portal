package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TimeZoneOffsetFormater;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementStatementReport;
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
public class GenerateSettlementStatementReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementStatementReportController.class);

    private final GenerateSettlementStatementReport generateSettlementStatementReport;

    @PostMapping("/secured/generateSettlementStatementReport")
    public ResponseEntity<Response> execute(
        @RequestParam("fspId") String fspId,
        @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate,
        @RequestParam("fileType") String fileType,
        @RequestParam("currencyId") String currencyId, @RequestParam("timezoneOffset") String timezoneOffset)
        throws DomainException, JsonProcessingException {

        LOG.info(
            "Generate Settlement Statement Report : startDate = [{}], endDate = [{}], fsp = [{}], fileType = [{}], timezoneOffset = [{}], currencyId = [{}]",
            startDate,
            endDate,
            fspId,
            fileType,
            timezoneOffset,
            currencyId);

        String timezone = TimeZoneOffsetFormater.normalizeOffsetFormat(timezoneOffset);

        GenerateSettlementStatementReport.Output output = this.generateSettlementStatementReport.execute(
            new GenerateSettlementStatementReport.Input(fspId,
                                                        Instant.parse(startDate),
                                                        Instant.parse(endDate),
                                                        fileType,
                                                        currencyId, timezone
            ));

        var response = new Response(output.statementData());

        LOG.info("Generate Settlement Statement Report Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("rptByte") byte[] statementReportByte) { }

}
