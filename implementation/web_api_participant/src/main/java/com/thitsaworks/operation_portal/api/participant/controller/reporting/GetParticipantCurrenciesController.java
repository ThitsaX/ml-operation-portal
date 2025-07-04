package com.thitsaworks.operation_portal.api.participant.controller.reporting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetParticipantCurrencies;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetParticipantCurrenciesController {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantCurrenciesController.class);

    private final GetParticipantCurrencies getParticipantCurrencies;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/get_participant_currency")
    public ResponseEntity<Response> execute(@RequestParam("dfspId") String dfspId)
            throws DomainException, JsonProcessingException {

        LOG.info("Get participant currency request : dfspId = {}", dfspId);

        var output = this.getParticipantCurrencies.execute(new GetParticipantCurrencies.Input(dfspId));

        List<CurrencyInfo> currencyInfoList = output.currencyDataList().stream()
                                                    .map(idType -> new CurrencyInfo(idType.getCurrency()))
                                                    .toList();

        var response = new Response(currencyInfoList);

        LOG.info("Get participant currency response : {}", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("hub_currency_list") List<CurrencyInfo> currencyInfoList
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CurrencyInfo(
            @JsonProperty("currency") String currency
    ) {}

}
