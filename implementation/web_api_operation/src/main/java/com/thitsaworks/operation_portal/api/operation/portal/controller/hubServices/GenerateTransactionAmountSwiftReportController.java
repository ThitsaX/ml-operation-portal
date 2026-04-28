package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TimeZoneOffsetFormater;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementBankReport;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateTransactionAmountSwiftReport;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateTransactionDetailReport;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class GenerateTransactionAmountSwiftReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateTransactionAmountSwiftReportController.class);

    private final GenerateTransactionAmountSwiftReport generateTransactionAmountSwiftReport;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/generateTransactionAmountReport")
    public ResponseEntity<Response> execute(@RequestParam("settlementId") String settlementId,
                                            @RequestParam("currencyId") String currencyId,

                                            @RequestParam("timezoneOffset") String timezoneOffset)
        throws DomainException, JsonProcessingException {

        LOG.info(
            "Generate Settlement Bank Report : settlementId = [{}], currencyId = [{}], fileType = [{}], timezoneOffset = [{}]",
            settlementId,
            currencyId,

            timezoneOffset);

        String timezone = TimeZoneOffsetFormater.normalizeOffsetFormat(timezoneOffset);

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        GenerateTransactionAmountSwiftReport.Output output = this.generateTransactionAmountSwiftReport.execute(
            new GenerateTransactionAmountSwiftReport.Input(settlementId,
                                                           currencyId,

                                                           timezone,
                                                           userContext.userId()
                                                                      .getId()));

        var response = new Response(output.reportData());

        LOG.info("Generate Transaction Detail Report Response : [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("rptByte") byte[] transactionAmountByte) implements Serializable { }

}
