package com.thitsaworks.dfsp_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsa.dfsp_portal.usecase.central_ledger.GetSettlementId;
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
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetSettlementIdController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementIdController.class);

    @Autowired
    private GetSettlementId getSettlementId;

    @RequestMapping(value = "/secured/get_settlement_id", method = RequestMethod.GET)
    public ResponseEntity<Response> execute(
            @RequestParam("start_date") String startDate,
            @RequestParam("end_date") String endDate,
            @RequestParam("timezoneOffset") String timezoneOffset) throws DFSPPortalException {

        GetSettlementId.Output output = this.getSettlementId.execute(
                new GetSettlementId.Input(Instant.parse(startDate), Instant.parse(endDate),timezoneOffset));

        List<Response.SettlementIdInfo> settlementIdInfoList = new ArrayList<>();

        for (var idType : output.getSettlementIds()) {

            settlementIdInfoList.add(new Response.SettlementIdInfo(idType.getSettlementId()));
        }

        return new ResponseEntity<>(new Response(settlementIdInfoList), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("settlement_id_list")
        protected List<SettlementIdInfo> settlementIdDataList;

        @Getter
        @AllArgsConstructor
        public static class SettlementIdInfo implements Serializable {

            @JsonProperty("settlement_id")
            protected String SettlementId;

        }
    }
}
