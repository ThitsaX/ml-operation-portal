package com.thitsaworks.operation_portal.api.participant.controller.reporting;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.hub_services.GenerateDetailReport;
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

    private final ObjectMapper objectMapper;

    @PostMapping("/secured/generate_detail_report")
    public ResponseEntity<Response> execute(@RequestParam("settlement_id") String settlementId,
                                            @RequestParam("fspid") String fspId,
                                            @RequestParam("file_type") String fileType,
                                            @RequestParam("timezoneOffset") String timezoneOffset)
            throws DomainException, JsonProcessingException {

        LOG.info(
                "Settlement details report filters : settlementId = {}, fspId = {}, fileType ={}, timezoneOffset = {}",
                settlementId,
                fspId,
                fileType,
                timezoneOffset);

        GenerateDetailReport.Output output = this.generateDetailReport.execute(
                new GenerateDetailReport.Input(fspId, settlementId, fileType,timezoneOffset));

        var response = new Response(output.detailReportData());

        LOG.info("Settlement details report response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Response(
            @JsonProperty("detail_report_byte") byte[] detailReportByte
    ) {}

}
