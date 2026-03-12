package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TimeZoneOffsetFormater;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementBankReportV2;
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

@RestController
@RequiredArgsConstructor
public class GenerateSettlementBankReportV2Controller {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementBankReportV2Controller.class);

    private final GenerateSettlementBankReportV2 generateSettlementBankReportV2;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/generateSettlementBankReportV2")
    public ResponseEntity<Response> execute(@RequestParam("settlementId") String settlementId,
                                            @RequestParam("currencyId") String currencyId,
                                            @RequestParam("fileType") String fileType,
                                            @RequestParam("timezoneOffset") String timezoneOffset)
        throws DomainException, JsonProcessingException {

        LOG.info(
            "Generate Settlement Bank Report V2 : settlementId = [{}], currencyId = [{}], fileType = [{}], timezoneOffset = [{}]",
            settlementId,
            currencyId,
            fileType,
            timezoneOffset);

        String timezone = TimeZoneOffsetFormater.normalizeOffsetFormat(timezoneOffset);

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        GenerateSettlementBankReportV2.Output output = this.generateSettlementBankReportV2.execute(
            new GenerateSettlementBankReportV2.Input(settlementId, currencyId, fileType, timezone, userContext.userId()
                                                                                                            .getId()));

        var response = new Response(output.reportData());

        LOG.info("Generate Settlement Bank Report V2 Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Response(@JsonProperty("rptByte") byte[] detailReportByte) implements Serializable { }

}
