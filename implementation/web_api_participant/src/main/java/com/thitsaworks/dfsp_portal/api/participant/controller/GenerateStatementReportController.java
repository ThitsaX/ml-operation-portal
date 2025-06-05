package com.thitsaworks.dfsp_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsa.dfsp_portal.usecase.central_ledger.GenerateStatementReport;
import com.thitsaworks.dfsp_portal.component.exception.DFSPPortalException;
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
public class GenerateStatementReportController {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateStatementReportController.class);

    @Autowired
    private GenerateStatementReport generateStatementReport;

    @RequestMapping(value = "/secured/generate_statement_report", method = RequestMethod.POST)
    public ResponseEntity<Response> execute(
            @RequestParam("start_date") String startDate, @RequestParam("end_date") String endDate,
            @RequestParam("fsp_id") String fspId,
            @RequestParam("file_type") String fileType,
            @RequestParam("timezoneoffset") String timeZoneOffset,
            @RequestParam("currencyId") String currencyId) throws DFSPPortalException {

        GenerateStatementReport.Output output = this.generateStatementReport.execute(
                new GenerateStatementReport.Input(Instant.parse(startDate), Instant.parse(endDate), fspId, fileType,timeZoneOffset,currencyId));

        return new ResponseEntity<>(new Response(output.getStatementData()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("detail_report_byte")
        private byte[] statementReportByte;


    }

}
