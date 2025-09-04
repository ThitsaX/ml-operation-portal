package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.api.GetHubSettlementWindows;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementWindows;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetSettlementWindowsController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementWindowsController.class);

    private final GetSettlementWindows getSettlementWindows;

    @GetMapping("/secured/settlementWindows")
    public ResponseEntity<List<GetHubSettlementWindows.SettlementWindow>> execute(
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestParam(value = "currency", required = false) String currency,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "participantId", required = false) Integer participantId
    ) throws DomainException {

        LOG.info(
                "Get Settlement Windows Request: FromDateTime =[{}], ToDateTime =[{}], Currency =[{}],State  = [{}],ParticipantId =[{}] ",
                fromDate, toDate, currency, state, participantId);

        var output = this.getSettlementWindows.execute(
                new GetSettlementWindows.Input(fromDate,
                        toDate,
                        currency,
                        state,
                        participantId)
        );

        LOG.info("Get Settlement Windows Response: [{}]", output.settlementWindows());
        return ResponseEntity.ok(output.settlementWindows());
    }

}