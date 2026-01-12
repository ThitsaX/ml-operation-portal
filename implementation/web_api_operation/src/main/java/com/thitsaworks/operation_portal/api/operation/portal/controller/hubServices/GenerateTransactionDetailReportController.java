package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TimeZoneOffsetFormater;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateTransactionDetailReport;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class GenerateTransactionDetailReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateTransactionDetailReportController.class);

    private final GenerateTransactionDetailReport generateTransactionDetailReport;

    @PostMapping("/secured/generateTransactionDetailReport")
    public ResponseEntity<Response> execute(@RequestParam("startDate") String startDate,
                                            @RequestParam("endDate") String endDate,
                                            @RequestParam("state") String state,
                                            @RequestParam("fileType") String fileType,
                                            @RequestParam("timezoneOffset") String timezoneOffset)
            throws DomainException, JsonProcessingException {

        LOG.info(
                "Generate Transaction Detail Report Request : startDate = [{}], state = [{}], fileType = [{}], timezoneOffset = [{}]",
                startDate,
                endDate,
                state,
                fileType,
                timezoneOffset);

        String timezone = TimeZoneOffsetFormater.normalizeOffsetFormat(timezoneOffset);

        GenerateTransactionDetailReport.Output output =
                this.generateTransactionDetailReport.execute(new GenerateTransactionDetailReport.Input(Instant.parse(
                        startDate), Instant.parse(endDate), state, fileType, timezone));

        var response = new Response(output.reportData());

        LOG.info("Generate Transaction Detail Report Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("rptByte") byte[] settlementByte) implements Serializable {}

}
