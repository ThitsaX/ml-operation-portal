package com.thitsaworks.operation_portal.api.participant.controller.reporting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetHubCurrencies;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetHubCurrenciesController {

    private static final Logger LOG = LoggerFactory.getLogger(GetHubCurrenciesController.class);

    private final GetHubCurrencies getHubCurrencies;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/get_hub_currency")
    public ResponseEntity<Response> execute() throws OperationPortalException, JsonProcessingException {

        LOG.info("Get hub currency request : {}", "");

        var output = this.getHubCurrencies.execute(new GetHubCurrencies.Input());

        List<CurrencyInfo> currencyInfoList = output.currencyDataList().stream()
                                                    .map(idType -> new CurrencyInfo(idType.getCurrency()))
                                                    .toList();

        var response = new Response(currencyInfoList);

        LOG.info("Get hub currency response : {}", this.objectMapper.writeValueAsString(response));

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
