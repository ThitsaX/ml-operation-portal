package com.thitsaworks.operation_portal.api.operation.portal.controller.hubServices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetSettlementListController {

    private static final Logger LOG = LoggerFactory.getLogger(GetSettlementListController.class);

    private final GetSettlementList getSettlementList;

    private final ObjectMapper objectMapper;

    @GetMapping("/secured/getSettlementList")
    public ResponseEntity<Response> execute(@RequestParam(
                                                value = "currency",
                                                required = false) String currency,
                                            @RequestParam(
                                                value = "participantId",
                                                required = false)
                                            Integer participantId,
                                            @RequestParam(
                                                value = "settlementWindowId",
                                                required = false)
                                            Integer settlementWindowId,
                                            @RequestParam(
                                                value = "accountId",
                                                required = false) Integer accountId,
                                            @RequestParam(
                                                value = "state",
                                                required = false) String state,
                                            @RequestParam(
                                                value = "fromDate",
                                                required = false) String fromDate,
                                            @RequestParam(
                                                value = "toDate",
                                                required = false) String toDate,
                                            @RequestParam(
                                                value = "fromSettlementWindowDateTime",
                                                required = false)
                                            String fromSettlementWindowDateTime,
                                            @RequestParam(
                                                value = "toSettlementWindowDateTime",
                                                required = false)
                                            String toSettlementWindowDateTime)
        throws DomainException, JsonProcessingException {

        LOG.info(
            "Get Settlements By Params : currency = [{}], participantId = [{}], settlementWindowId = [{}], accountId = [{}], state = [{}], fromDate = [{}], toDate = [{}], fromSettlementWindowDateTime = [{}], toSettlementWindowDateTime = [{}]",
            currency,
            participantId,
            settlementWindowId,
            accountId,
            state,
            fromDate,
            toDate,
            fromSettlementWindowDateTime,
            toSettlementWindowDateTime);

        GetSettlementList.Output output = this.getSettlementList.execute(
            new GetSettlementList.Input(currency,
                                        participantId,
                                        settlementWindowId,
                                        accountId,
                                        state,
                                        fromDate,
                                        toDate,
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
                                                         account.getNetSettlementAmount()));
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

            settlementList.sort(Comparator.comparing(Response.Settlement::createdDate)
                                          .reversed());

        }

        var response = new Response(settlementList);

        LOG.info("Get Settlements By Params Response : [{}]", this.objectMapper.writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public record Response(@JsonProperty("settlementList") List<Settlement> settlementList) implements Serializable {

        public record Settlement(@JsonProperty("settlementId") Integer settlementId,
                                 @JsonProperty("state") String state,
                                 @JsonProperty("reason") String reason,
                                 @JsonProperty("createdDate") String createdDate,
                                 @JsonProperty("changedDate") String changedDate,
                                 @JsonProperty("settlementWindowList") List<SettlementWindow> settlementWindowList,
                                 @JsonProperty("participantList") List<Participant> participantList) {
        }

        public record SettlementWindow(@JsonProperty("settlementWindowId") Integer settlementWindowId,
                                       @JsonProperty("state") String state,
                                       @JsonProperty("reason") String reason,
                                       @JsonProperty("createdDate") String createdDate,
                                       @JsonProperty("changedDate") String changedDate,
                                       @JsonProperty("contentList") List<Content> contentList) {
        }

        public record Content(@JsonProperty("contentId") Integer contentId,
                              @JsonProperty("state") String state,
                              @JsonProperty("ledgerAccountType") String ledgerAccountType,
                              @JsonProperty("currencyId") String currencyId,
                              @JsonProperty("createdDate") String createdDate,
                              @JsonProperty("changedDate") String changedDate) {
        }

        public record Participant(@JsonProperty("participantId") Integer participantId,
                                  @JsonProperty("accountList") List<Account> accountList) {
        }

        public record Account(@JsonProperty("accountId") Integer accountId,
                              @JsonProperty("state") String state,
                              @JsonProperty("reason") String reason,
                              @JsonProperty("externalReference") String externalReference,
                              @JsonProperty("createdDate") String createdDate,
                              @JsonProperty("netSettlementAmount") Money netSettlementAmount) {
        }

    }

}
