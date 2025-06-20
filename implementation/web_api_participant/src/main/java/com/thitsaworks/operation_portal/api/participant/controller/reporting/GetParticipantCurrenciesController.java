package com.thitsaworks.operation_portal.api.participant.controller.reporting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetParticipantCurrencies;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetParticipantCurrenciesController {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantCurrenciesController.class);

    private final GetParticipantCurrencies getParticipantCurrencies;

    @GetMapping(value = "/secured/get_participant_currency")
    public ResponseEntity<Response> execute(@RequestParam("dfspId") String dfspId) throws OperationPortalException {

        GetParticipantCurrencies.Output output =
                this.getParticipantCurrencies.execute(new GetParticipantCurrencies.Input(dfspId));

        List<Response.CurrencyInfo> currencyInfoList = new ArrayList<>();

        for (var idType : output.currencyDataList()) {
            currencyInfoList.add(new Response.CurrencyInfo(idType.getCurrency()));
        }

        return new ResponseEntity<>(new Response(currencyInfoList), HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(@JsonProperty("hub_currency_list") List<CurrencyInfo> currencyInfoList)
            implements Serializable {

        record CurrencyInfo(@JsonProperty("currency") String currency) implements Serializable {}

    }

}
