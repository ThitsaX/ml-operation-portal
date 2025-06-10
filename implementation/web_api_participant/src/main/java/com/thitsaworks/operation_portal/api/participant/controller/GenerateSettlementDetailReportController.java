package com.thitsaworks.operation_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.usecase.central_ledger.GenerateDetailReport;
import com.thitsaworks.operation_portal.component.exception.DFSPPortalException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
public class GenerateSettlementDetailReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementDetailReportController.class);

    @Autowired
    private GenerateDetailReport generateDetailReport;

    @RequestMapping(value = "/secured/generate_detail_report", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(@RequestParam("settlement_id") String settlementId,
                                            @RequestParam("fspid") String fspId,
                                            @RequestParam("file_type") String fileType,
                                            @RequestParam("timezoneOffset") String timezoneOffset)
            throws DFSPPortalException {

        GenerateDetailReport.Output output = this.generateDetailReport.execute(
                new GenerateDetailReport.Input(fspId, settlementId, fileType,timezoneOffset));

        return new ResponseEntity<>(new Response(
                        output.getDetailReportData()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("detail_report_byte")
        private byte[] detailReportByte;

    }

}
