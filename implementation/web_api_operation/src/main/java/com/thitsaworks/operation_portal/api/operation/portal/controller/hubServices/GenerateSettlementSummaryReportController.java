package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.api.operation.portal.security.UserContext;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TimeZoneOffsetFormater;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementSummaryReport;
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
public class GenerateSettlementSummaryReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementSummaryReportController.class);

    private final GenerateSettlementSummaryReport generateSettlementSummaryReport;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/generateSettlementReport")
    public ResponseEntity<Response> execute
        (@RequestParam("fspId") String fspId,
         @RequestParam("settlementId") String settlementId,
         @RequestParam("fileType") String fileType,
         @RequestParam("timezoneOffset") String timezoneOffset)
        throws DomainException, JsonProcessingException {

        LOG.info(
            "Generate Settlement Report : fspId = [{}], settlementId = [{}], fileType = [{}], timezoneOffset = [{}]",
            fspId, settlementId, fileType, timezoneOffset);

        String timezone = TimeZoneOffsetFormater.normalizeOffsetFormat(timezoneOffset);

        UserContext userContext =
            (UserContext) SecurityContextHolder.getContext()
                                               .getAuthentication()
                                               .getDetails();

        GenerateSettlementSummaryReport.Output output = this.generateSettlementSummaryReport.execute(
            new GenerateSettlementSummaryReport.Input(fspId,
                                                      settlementId,
                                                      fileType,
                                                      timezone,
                                                      userContext.userId()
                                                                 .getId()));

        var response = new Response(output.settlementByte());

        LOG.info("Generate Settlement Report Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("rptByte") byte[] settlementByte) implements Serializable { }

}
