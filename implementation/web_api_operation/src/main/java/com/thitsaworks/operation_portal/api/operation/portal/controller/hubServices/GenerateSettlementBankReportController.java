package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementBankReport;
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
public class GenerateSettlementBankReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementBankReportController.class);

    private final GenerateSettlementBankReport generateSettlementBankReport;

    @PostMapping("/secured/generateSettlementBankReport")
    public ResponseEntity<Response> execute(@RequestParam("settlementId") String settlementId,
                                            @RequestParam("currencyId") String currencyId,
                                            @RequestParam("fileType") String fileType,
                                            @RequestParam("timezoneOffset") String timezoneOffset)
            throws DomainException, JsonProcessingException {

        LOG.info(
                "Generate Settlement Bank Report : settlementId = [{}], currencyId = [{}], fileType = [{}], timezoneOffset = [{}]",
                settlementId,
                currencyId,
                fileType,
                timezoneOffset);

        GenerateSettlementBankReport.Output output = this.generateSettlementBankReport.execute(
                new GenerateSettlementBankReport.Input(settlementId, currencyId, fileType, timezoneOffset));

        var response = new Response(output.reportData());

        LOG.info("Generate Settlement Bank Report Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Response(@JsonProperty("rptByte") byte[] detailReportByte) implements Serializable {}

}
