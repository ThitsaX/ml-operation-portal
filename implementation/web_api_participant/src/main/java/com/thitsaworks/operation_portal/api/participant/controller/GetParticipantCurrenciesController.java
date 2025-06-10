package com.thitsaworks.operation_portal.api.participant.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetParticipantCurrencies;
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
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetParticipantCurrenciesController {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantCurrenciesController.class);

    @Autowired
    private GetParticipantCurrencies getParticipantCurrencies;

    @RequestMapping(value = "/secured/get_participant_currency", method = RequestMethod.GET)
    public ResponseEntity<Response> execute(@RequestParam("dfspId") String dfspId) throws DFSPPortalException {

        GetParticipantCurrencies.Output output = this.getParticipantCurrencies.execute(new GetParticipantCurrencies.Input(dfspId));

        List<Response.CurrencyInfo> currencyInfoList = new ArrayList<>();

        for (var idType : output.getCurrencyDataList()) {

            currencyInfoList.add(new Response.CurrencyInfo(
                            idType.getCurrency()));
        }

        return new ResponseEntity<>(new Response(currencyInfoList), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Response implements Serializable {

        @JsonProperty("hub_currency_list")
        protected List<CurrencyInfo> currencyInfoList;

        @Getter
        @AllArgsConstructor
        public static class CurrencyInfo implements Serializable {

            @JsonProperty("currency")
            protected String currency;

        }

    }
}
