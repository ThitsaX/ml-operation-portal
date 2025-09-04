package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.support.Settlement;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementsByParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetSettlementsByParamController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementsByParamController.class);

    private final GetSettlementsByParam getSettlementsByParam;

    @GetMapping("/secured/getSettlementsByParam")
    public ResponseEntity<Response> execute(@RequestParam(value = "currency", required = false) String currency,
                                            @RequestParam(value = "participantId", required = false)
                                            Integer participantId,
                                            @RequestParam(value = "settlementWindowId", required = false)
                                            Integer settlementWindowId,
                                            @RequestParam(value = "accountId", required = false) Integer accountId,
                                            @RequestParam(value = "state", required = false) String state,
                                            @RequestParam(value = "fromDateTime", required = false) String fromDateTime,
                                            @RequestParam(value = "toDateTime", required = false) String toDateTime,
                                            @RequestParam(value = "fromSettlementWindowDateTime", required = false)
                                            String fromSettlementWindowDateTime,
                                            @RequestParam(value = "toSettlementWindowDateTime", required = false)
                                            String toSettlementWindowDateTime)
            throws DomainException, JsonProcessingException {

        LOG.info(
                "Get settlements by param : currency = [{}], participantId = [{}], settlementWindowId = [{}], accountId = [{}], state = [{}], fromDateTime = [{}], toDateTime = [{}], fromSettlementWindowDateTime = [{}], toSettlementWindowDateTime = [{}]",
                currency,
                participantId,
                settlementWindowId,
                accountId,
                state,
                fromDateTime,
                toDateTime,
                fromSettlementWindowDateTime,
                toSettlementWindowDateTime);

        GetSettlementsByParam.Output output = this.getSettlementsByParam.execute(
                new GetSettlementsByParam.Input(currency,
                                                participantId,
                                                settlementWindowId,
                                                accountId,
                                                state,
                                                fromDateTime,
                                                toDateTime,
                                                fromSettlementWindowDateTime,
                                                toSettlementWindowDateTime));

        var response = new Response(output.settlementList());

        LOG.info("Generate detail report Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Response(@JsonProperty("settlementList") List<Settlement> settlementList) implements Serializable {}

}
