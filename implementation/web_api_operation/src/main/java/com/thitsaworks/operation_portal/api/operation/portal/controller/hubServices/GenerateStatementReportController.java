package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.hub_services.GenerateStatementReport;
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

    @PostMapping("/secured/generateStatementReport")
    public ResponseEntity<Response> execute(
        @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
        @RequestParam("fspId") String fspId,
        @RequestParam("fileType") String fileType,
        @RequestParam("timeZoneOffset") String timeZoneOffset,
        @RequestParam("currencyId") String currencyId) throws DomainException, JsonProcessingException {

        GenerateStatementReport.Output output = this.generateStatementReport.execute(
            new GenerateStatementReport.Input(Instant.parse(startDate),
                                              Instant.parse(endDate),
                                              fspId,
                                              fileType,
                                              timeZoneOffset,
                                              currencyId));

        var response = new Response(output.statementData());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("rptByte") byte[] statementReportByte) { }

}
