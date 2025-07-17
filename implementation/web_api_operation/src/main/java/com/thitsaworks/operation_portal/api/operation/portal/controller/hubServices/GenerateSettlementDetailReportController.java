package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateDetailReport;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GenerateSettlementDetailReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementDetailReportController.class);

    private final GenerateDetailReport generateDetailReport;

    @PostMapping("/secured/generateDetailReport")
    public ResponseEntity<Response> execute(@RequestParam("settlementId") String settlementId,
                                            @RequestParam("fspId") String fspId,
                                            @RequestParam("fileType") String fileType,
                                            @RequestParam("timezoneOffset") String timezoneOffset)
        throws DomainException, JsonProcessingException {

        GenerateDetailReport.Output output = this.generateDetailReport.execute(
            new GenerateDetailReport.Input(fspId, settlementId, fileType, timezoneOffset));

        var response = new Response(output.detailReportData());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Response(
        @JsonProperty("rptByte") byte[] detailReportByte
    ) { }

}
