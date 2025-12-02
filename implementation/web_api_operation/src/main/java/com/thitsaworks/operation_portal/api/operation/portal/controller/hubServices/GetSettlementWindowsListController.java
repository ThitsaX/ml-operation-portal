package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindowState;
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

        if (state != null && state.equalsIgnoreCase("PENDING")) {
            state = "PENDING_SETTLEMENT";
        }

        var output = this.getSettlementWindowsList.execute(
            new GetSettlementWindowsList.Input(fromDate,
                                               toDate,
                                               currency,
                                               state,
                                               participantId)
                                                          );

        List<Response.SettlementWindow> settlementWindowList = new ArrayList<>();

        for (var settlementWindow : output.settlementWindows()) {

            String settlementWindowState = settlementWindow.state();
            if ("PENDING_SETTLEMENT".equalsIgnoreCase(settlementWindow.state())) {
                settlementWindowState = String.valueOf(SettlementWindowState.PENDING);
            }
            List<Response.Content> contentList = new ArrayList<>();

            for (var content : settlementWindow.content()) {
                String contentState = content.getState();

                if ("PENDING_SETTLEMENT".equalsIgnoreCase(contentState)) {
                    contentState = String.valueOf(SettlementWindowState.PENDING);
                }

                contentList.add(new Response.Content(content.getId(),
                                                     contentState,
                                                     content.getLedgerAccountType(),
                                                     content.getCurrencyId(),
                                                     content.getCreatedDate(),
                                                     content.getChangedDate()));
            }

            settlementWindowList.add(new Response.SettlementWindow(settlementWindow.settlementWindowId(),
                                                                   settlementWindowState,
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
    public record Response(@JsonProperty("settlementWindowList") List<SettlementWindow> settlementWindowList) {

        public record SettlementWindow(@JsonProperty("settlementWindowId") Integer settlementWindowId,
                                       @JsonProperty("state") String state,
                                       @JsonProperty("reason") String reason,
                                       @JsonProperty("createdDate") String createdDate,
                                       @JsonProperty("changedDate") String changedDate,
                                       @JsonProperty("contentList") List<Content> contentList
        ) implements Serializable {

        }

        public record Content(
            @JsonProperty("contentId") Integer contentId,
            @JsonProperty("state") String state,
            @JsonProperty("ledgerAccountType") String ledgerAccountType,
            @JsonProperty("currencyId") String currencyId,
            @JsonProperty("createdDate") String createdDate,
            @JsonProperty("changedDate") String changedDate
        ) implements Serializable {

        }

    }

}