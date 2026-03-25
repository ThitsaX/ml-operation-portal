package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TimeZoneOffsetFormater;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementDetailReport;
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
public class GenerateSettlementDetailReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementDetailReportController.class);

    private final GenerateSettlementDetailReport generateSettlementDetailReport;

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/generateDetailReport")
    public ResponseEntity<Response> execute(@RequestParam("settlementId") String settlementId,
                                            @RequestParam("fspId") String fspId,
                                            @RequestParam("fileType") String fileType,
                                            @RequestParam("timezoneOffset") String timezoneOffset)
        throws DomainException, JsonProcessingException {

        LOG.info("Generate Detail Report : settlementId = [{}], fspId = [{}], fileType = [{}], timezoneOffset = [{}]",
                 settlementId, fspId, fileType, timezoneOffset);

        String timezone = TimeZoneOffsetFormater.normalizeOffsetFormat(timezoneOffset);

        GenerateSettlementDetailReport.Output output = this.generateSettlementDetailReport.execute(
            new GenerateSettlementDetailReport.Input(fspId, settlementId, fileType, timezone));

        var response = new Response(
            output.requestId().getEntityId().toString(),
            output.status().name(),
            output.fileUrl(),
            output.paramsSignature());

        LOG.info("Generate Detail Report Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Response(@JsonProperty("requestId") String requestId,
                           @JsonProperty("status") String status,
                           @JsonProperty("fileUrl") String fileUrl,
                           @JsonProperty("paramsSignature") String paramsSignature)
        implements Serializable { }

}
