package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateFeeSettlementReport;
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
public class GenerateFeeSettlementReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateFeeSettlementReportController.class);

    private final GenerateFeeSettlementReport generateFeeSettlementReport;

    @PostMapping("/secured/generateFeeReport")
    public ResponseEntity<Response> execute(
        @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
        @RequestParam("fromFspId") String fromFspId, @RequestParam("toFspId") String toFspId,
        @RequestParam("currency") String currency,
        @RequestParam("timezoneOffset") String timezoneOffset,
        @RequestParam("fileType") String fileType) throws DomainException, JsonProcessingException {

        LOG.info("Generate Fee Report Request : startDate = [{}], endDate = [{}], fromFspId = [{}], " +
                     "toFspId = [{}], currency = [{}], timezoneOffset = [{}], fileType = [{}]",
                 startDate, endDate, fromFspId, toFspId, currency, timezoneOffset, fileType);

        GenerateFeeSettlementReport.Output output = this.generateFeeSettlementReport.execute(
            new GenerateFeeSettlementReport.Input(Instant.parse(startDate), Instant.parse(endDate), fromFspId,
                                                  toFspId,
                                                  currency,
                                                  timezoneOffset,
                                                  fileType));

        var response = new Response(output.rptData());

        LOG.info("Generate Fee Report Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("rptByte") byte[] feeReportByte) implements Serializable { }

}
