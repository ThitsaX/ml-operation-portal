package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetSettlementListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementListController.class);

    private final GetSettlementList getSettlementList;

    @GetMapping("/secured/getSettlementList")
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
                "Get settlements by params : currency = [{}], participantId = [{}], settlementWindowId = [{}], accountId = [{}], state = [{}], fromDateTime = [{}], toDateTime = [{}], fromSettlementWindowDateTime = [{}], toSettlementWindowDateTime = [{}]",
                currency,
                participantId,
                settlementWindowId,
                accountId,
                state,
                fromDateTime,
                toDateTime,
                fromSettlementWindowDateTime,
                toSettlementWindowDateTime);

        GetSettlementList.Output output = this.getSettlementList.execute(
                new GetSettlementList.Input(currency,
                                                 participantId,
                                                 settlementWindowId,
                                                 accountId,
                                                 state,
                                                 fromDateTime,
                                                 toDateTime,
                                                 fromSettlementWindowDateTime,
                                                 toSettlementWindowDateTime));

        List<Response.Settlement> settlementList = new ArrayList<>();

        for (var settlement : output.settlementList()) {

            List<Response.SettlementWindow> settlementWindowsList = new ArrayList<>();

            for (var settlementWindow : settlement.getSettlementWindows()) {

                List<Response.Content> contentList = new ArrayList<>();

                for (var content : settlementWindow.getContent()) {
                    contentList.add(new Response.Content(content.getId(),
                                                         content.getState(),
                                                         content.getLedgerAccountType(),
                                                         content.getCurrencyId(),
                                                         content.getCreatedDate(),
                                                         content.getChangedDate()));
                }

                settlementWindowsList.add(new Response.SettlementWindow(settlementWindow.getId(),
                                                                        settlementWindow.getState(),
                                                                        settlementWindow.getReason(),
                                                                        settlementWindow.getCreatedDate(),
                                                                        settlementWindow.getChangedDate(),
                                                                        contentList));
            }

            List<Response.Participant> participantList = new ArrayList<>();

            for (var participant : settlement.getParticipants()) {

                List<Response.Account> accountList = new ArrayList<>();

                for (var account : participant.getAccounts()) {

                    accountList.add(new Response.Account(account.getId(),
                                                         account.getState(),
                                                         account.getReason(),
                                                         account.getExternalReference(),
                                                         account.getCreatedDate(),
                                                         new Response.NetSettlementAmount(account.getNetSettlementAmount()
                                                                                                 .getAmount(),
                                                                                          account.getNetSettlementAmount()
                                                                                                 .getCurrency())));
                }
                participantList.add(new Response.Participant(participant.getId(), accountList));
            }

            settlementList.add(new Response.Settlement(settlement.getId(),
                                                       settlement.getState(),
                                                       settlement.getReason(),
                                                       settlement.getCreatedDate(),
                                                       settlement.getChangedDate(),
                                                       settlementWindowsList,
                                                       participantList));
        }

        var response = new Response(settlementList);

        LOG.info("Get Settlements By Params Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Response(List<Settlement> settlementList) implements Serializable {

        public record Settlement(Integer id,
                                 String state,
                                 String reason,
                                 String createdDate,
                                 String changedDate,
                                 List<SettlementWindow> settlementWindowList,
                                 List<Participant> participantList) {
        }

        public record SettlementWindow(Integer settlementWindowId,
                                       String state,
                                       String reason,
                                       String createdDate,
                                       String changedDate,
                                       List<Content> contentList) {
        }

        public record Content(Integer contentId,
                              String state,
                              String ledgerAccountType,
                              String currencyId,
                              String createdDate,
                              String changedDate) {
        }

        public record Participant(Integer participantId,
                                  List<Account> accountList) {
        }

        public record Account(Integer accountId,
                              String state,
                              String reason,
                              String externalReference,
                              String createdDate,
                              NetSettlementAmount netSettlementAmount) {
        }

        public record NetSettlementAmount(BigDecimal amount,
                                          String currency) {
        }

    }

}
