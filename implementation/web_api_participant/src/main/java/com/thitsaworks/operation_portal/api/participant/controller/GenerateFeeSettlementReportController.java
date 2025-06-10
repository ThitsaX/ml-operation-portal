package com.thitsaworks.operation_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.usecase.central_ledger.GenerateFeeSettlementReport;
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
import java.time.Instant;

@RestController
public class GenerateFeeSettlementReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateFeeSettlementReportController.class);

    @Autowired
    private GenerateFeeSettlementReport generateFeeSettlementReport;

    @RequestMapping(value = "/secured/generate_fee_report", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(
            @RequestParam("start_date") String startDate, @RequestParam("end_date") String endDate,
            @RequestParam("from_fsp_id") String fromFspId, @RequestParam("to_fsp_id") String toFspId,
            @RequestParam("currency") String currency,
            @RequestParam("time_zone_offset") String timezoneoffset,
            @RequestParam("file_type") String fileType) throws DFSPPortalException {

        GenerateFeeSettlementReport.Output output = this.generateFeeSettlementReport.execute(
                new GenerateFeeSettlementReport.Input(Instant.parse(startDate), Instant.parse(endDate), fromFspId,
                        toFspId,currency, timezoneoffset, fileType
                        ));

        return new ResponseEntity<>(new Response(output.getRptData()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("rpt_byte")
        private byte[]  feeReportByte;

    }

}
