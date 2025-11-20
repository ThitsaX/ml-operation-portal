package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementWindowsList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetSettlementWindowsListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementWindowsListController.class);

    private final GetSettlementWindowsList getSettlementWindowsList;

    @GetMapping("/secured/getSettlementWindowsList")
    public ResponseEntity<Response> execute(
        @RequestParam("fromDate") String fromDate,
        @RequestParam("toDate") String toDate,
        @RequestParam(
            value = "currency",
            required = false) String currency,
        @RequestParam(
            value = "state",
            required = false) String state,
        @RequestParam(
            value = "participantId",
            required = false) Integer participantId
                                           )
        throws DomainException {

        LOG.info(
            "Get Settlement Windows By Params Request : FromDateTime =[{}], ToDateTime =[{}], Currency =[{}],State  = [{}],ParticipantId =[{}] ",
            fromDate,
            toDate,
            currency,
            state,
            participantId);

        var output = this.getSettlementWindowsList.execute(
            new GetSettlementWindowsList.Input(fromDate,
                                               toDate,
                                               currency,
                                               state,
                                               participantId)
                                                          );

        List<Response.SettlementWindow> settlementWindowList = new ArrayList<>();

        for (var settlementWindow : output.settlementWindows()) {

            List<Response.Content> contentList = new ArrayList<>();

            for (var content : settlementWindow.content()) {
                contentList.add(new Response.Content(content.getId(),
                                                     content.getState(),
                                                     content.getLedgerAccountType(),
                                                     content.getCurrencyId(),
                                                     content.getCreatedDate(),
                                                     content.getChangedDate()));
            }

            settlementWindowList.add(new Response.SettlementWindow(settlementWindow.settlementWindowId(),
                                                                   settlementWindow.state(),
                                                                   settlementWindow.reason(),
                                                                   settlementWindow.createdDate(),
                                                                   settlementWindow.changedDate(),
                                                                   contentList));

            settlementWindowList.sort(Comparator.comparing(Response.SettlementWindow::createdDate)
                                                .reversed());

        }
        LOG.info("Get Settlement Windows By Params Response : [{}]", settlementWindowList);

        return ResponseEntity.ok(new Response(settlementWindowList));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(List<SettlementWindow> settlementWindowList) {

        public record SettlementWindow(
            Integer settlementWindowId,
            String state,
            String reason,
            String createdDate,
            String changedDate,
            List<Content> contentList
        ) implements Serializable {

        }

        public record Content(
            Integer contentId,
            String state,
            String ledgerAccountType,
            String currencyId,
            String createdDate,
            String changedDate
        ) implements Serializable {

        }

    }

}