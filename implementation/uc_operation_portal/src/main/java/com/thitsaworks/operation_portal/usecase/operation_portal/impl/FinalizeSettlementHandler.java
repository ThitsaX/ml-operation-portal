package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.PositionActionType;
import com.thitsaworks.operation_portal.component.fspiop.model.Extension;
import com.thitsaworks.operation_portal.component.fspiop.model.ExtensionList;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.approval.data.ApprovalRequestData;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.ParticipantHubClient;
import com.thitsaworks.operation_portal.core.hub_services.SettlementHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import com.thitsaworks.operation_portal.core.hub_services.api.PutUpdateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantDetailData;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowInfoData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetNetTransferAmountBySettlementIdQuery;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantPositionsDataByParticipantNameAndCurrencyQuery;
import com.thitsaworks.operation_portal.core.hub_services.query.HubParticipantQuery;
import com.thitsaworks.operation_portal.core.hub_services.support.Settlement;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAccount;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAction;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementLedgerAccountTypes;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementParticipant;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementState;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantNDCQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.FinalizeSettlement;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetNetTransferAmountBySettlementId;
import com.thitsaworks.operation_portal.usecase.util.HandleUpdateNdc;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class FinalizeSettlementHandler
    extends OperationPortalAuditableUseCase<FinalizeSettlement.Input, FinalizeSettlement.Output>
    implements FinalizeSettlement {

    private static final Logger LOG = LoggerFactory.getLogger(FinalizeSettlementHandler.class);

    private final SettlementHubClient settlementHubClient;

    private final ParticipantHubClient participantHubClient;

    private final HubParticipantQuery hubParticipantQuery;

    private final GetNetTransferAmountBySettlementIdQuery getNetTransferAmountBySettlementIdQuery;

    private final AtomicBoolean isSettlementFinalized = new AtomicBoolean(false);

    private final ParticipantNDCQuery participantNDCQuery;

    private final HandleUpdateNdc handleUpdateNdc;

    private final GetParticipantPositionsDataByParticipantNameAndCurrencyQuery
            participantPositionsDataByParticipantNameAndCurrencyQuery;


    @Autowired
    public FinalizeSettlementHandler(CreateInputAuditCommand createInputAuditCommand,
                                     CreateOutputAuditCommand createOutputAuditCommand,
                                     CreateExceptionAuditCommand createExceptionAuditCommand,
                                     ObjectMapper objectMapper,
                                     PrincipalCache principalCache,
                                     ActionAuthorizationManager actionAuthorizationManager,
                                     SettlementHubClient settlementHubClient,
                                     ParticipantHubClient participantHubClient,
                                     HubParticipantQuery hubParticipantQuery,
                                     GetNetTransferAmountBySettlementIdQuery getNetTransferAmountBySettlementIdQuery,
                                     ParticipantNDCQuery participantNDCQuery, HandleUpdateNdc handleUpdateNdc,
                                     GetParticipantPositionsDataByParticipantNameAndCurrencyQuery participantPositionsDataByParticipantNameAndCurrencyQuery
                                    ) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.settlementHubClient = settlementHubClient;
        this.participantHubClient = participantHubClient;
        this.hubParticipantQuery = hubParticipantQuery;
        this.getNetTransferAmountBySettlementIdQuery = getNetTransferAmountBySettlementIdQuery;
        this.participantNDCQuery = participantNDCQuery;
        this.handleUpdateNdc = handleUpdateNdc;
        this.participantPositionsDataByParticipantNameAndCurrencyQuery =
                participantPositionsDataByParticipantNameAndCurrencyQuery;
    }

    @Override
    public Output onExecute(Input input) throws DomainException, ConnectException {

        try {

            {
                GetNetTransferAmountBySettlementIdQuery.Output
                    output =
                    this.getNetTransferAmountBySettlementIdQuery.execute(new GetNetTransferAmountBySettlementIdQuery.Input(
                        input.settlementId()));

                List<GetNetTransferAmountBySettlementId.Detail> details = new ArrayList<>();

                for (SettlementWindowInfoData windowInfo : output.getWindowInfoList()) {

                    if (windowInfo.getCredit() != null && windowInfo.getCredit()
                                                                    .abs()
                                                                    .compareTo(windowInfo.getParticipantBalance()
                                                                                         .abs()) > 0) {

                        throw new ParticipantException(ParticipantErrors.ORG_INSUFFICIENT_BALANCE.format(windowInfo.getDfspName()));
                    }

                    GetNetTransferAmountBySettlementId.Detail detail = new GetNetTransferAmountBySettlementId.Detail(
                        windowInfo.getDfspName(),
                        windowInfo.getParticipantLimit(),
                        windowInfo.getParticipantBalance(),
                        windowInfo.getDebit(),
                        windowInfo.getCredit(),
                        windowInfo.getCurrencyId(),
                        windowInfo.getParticipantSettlementCurrencyId());

                    details.add(detail);
                }

                Settlement settlement = this.settlementHubClient.getSettlement(input.settlementId());

                PutUpdateSettlement.Request request = new PutUpdateSettlement.Request(settlement.getParticipants());

                List<SettlementParticipant> settlementParticipants = request.participants();

                SettlementState settlementState = SettlementState.valueOf(settlement.getState());

                if (SettlementState.SETTLED.equals(settlementState)) {
                    return new Output(true);
                }
                if (this.isSettlementFinalized.get()) {
                    return new Output(true);
                }

                this.isSettlementFinalized.set(true);

                // call putUpdateSettlement until the settlementState is settled.
                while (!settlementParticipants.getFirst()
                                              .getAccounts()
                                              .getFirst()
                                              .getState()
                                              .equals(SettlementState.SETTLED.toString())) {

                    switch (settlementState) {

                        case PS_TRANSFERS_RESERVED -> settlementState = SettlementState.PS_TRANSFERS_COMMITTED;
                        case PS_TRANSFERS_COMMITTED -> settlementState = SettlementState.SETTLED;
                    }

                    for (SettlementParticipant participant : settlementParticipants) {
                        for (SettlementAccount account : participant.getAccounts()) {
                            account.setState(settlementState.toString());
                        }
                    }

                    PutUpdateSettlement.Response
                        putUpdateSettlementResponse =
                        this.settlementHubClient.putUpdateSettlement(settlement.getId(),
                                                                     new PutUpdateSettlement.Request(
                                                                         settlementParticipants));

                    settlementState = SettlementState.valueOf(putUpdateSettlementResponse.state());

                }

                LOG.info(
                    "Updating settlement state for settlementId: [{}] is finished. Now calling postParticipantBalance apis for related participant accounts.",
                    settlement.getId());

                if (SettlementState.SETTLED.equals(settlementState)) {

                    // call postParticipantBalance for participant accounts
                    List<HubParticipantDetailData>
                        hubParticipantDetailDataList =
                        this.hubParticipantQuery.getHubParticipantDetailDataList();

                    ExtensionList extensionList = new ExtensionList();
                    extensionList.addExtensionItem(new Extension().key("settlementId")
                                                                  .value(settlement.getId()
                                                                                   .toString()));

                    for (SettlementParticipant participant : settlementParticipants) {

                        for (SettlementAccount account : participant.getAccounts()) {

                            String externalReference = "BOP settlement ID: " + settlement.getId();

                            String
                                reason =
                                "Settlement:" + settlement.getId();

                            //  TODO: to confirm
                            // if net amount is positive -> sender
                            // if net amount is negative -> receiver
                            BigDecimal amount = new BigDecimal(account.getNetSettlementAmount()
                                                                      .getAmount());
                            SettlementAction
                                settlementAction =
                                amount.signum() > 0 ? SettlementAction.recordFundsOutPrepareReserve :
                                    SettlementAction.recordFundsIn;

                            account.getNetSettlementAmount()
                                   .setAmount(amount.abs()
                                                    .toString());

                            HubParticipantDetailData
                                hubParticipantDetailData =
                                hubParticipantDetailDataList.stream()
                                                            .filter(participantDetailData -> participantDetailData.getParticipantId()
                                                                                                                  .equals(
                                                                                                                      participant.getId()))
                                                            .findFirst()
                                                            .orElse(null);

                            if (hubParticipantDetailData == null) {
                                throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_ERROR.defaultMessage(
                                    "Participant with ID [" + participant.getId() + "] cannot find on Hub"));
                            }

                            Integer
                                settleAccountId =
                                hubParticipantDetailData.getAccounts()
                                                        .stream()
                                                        .filter(acc -> acc.getCurrencyId()
                                                                          .equals(account.getNetSettlementAmount()
                                                                                         .getCurrency()
                                                                                         .toString()) &&
                                                                           acc.getLedgerAccountTypeName()
                                                                              .equals(SettlementLedgerAccountTypes.SETTLEMENT.toString()))
                                                        .map(HubParticipantDetailData.AccountData::getParticipantCurrencyId)
                                                        .findFirst()
                                                        .orElse(null);

                            if (settleAccountId == null) {
                                throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_ERROR.defaultMessage(
                                    "Account with Currency [" + account.getNetSettlementAmount()
                                                                       .getCurrency()
                                                                       .toString() + "] and Ledger Account Type [" +
                                        SettlementLedgerAccountTypes.SETTLEMENT.toString() + "] cannot find for [" +
                                        hubParticipantDetailData.getParticipantName() + "] on Hub"));
                            }

                            PostParticipantBalance.Request
                                postParticipantBalanceRequest =
                                new PostParticipantBalance.Request(UUID.randomUUID()
                                                                       .toString(),
                                                                   externalReference,
                                                                   settlementAction.toString(),
                                                                   reason,
                                                                   account.getNetSettlementAmount(),
                                                                   extensionList);

                            this.participantHubClient.postParticipantBalance(hubParticipantDetailData.getParticipantName(),
                                                                             settleAccountId.toString(),
                                                                             postParticipantBalanceRequest);

                            var ndcData = this.participantNDCQuery.get(hubParticipantDetailData.getParticipantName(),
                                                                       account.getNetSettlementAmount()
                                                                              .getCurrency()
                                                                              .toString());
                            boolean toRecalculateNDC = false;

                            BigDecimal
                                ndcPercent =
                                ndcData.map(ParticipantNDC::getNdcPercent)
                                       .orElse(BigDecimal.ZERO);

                            toRecalculateNDC = ndcPercent.compareTo(BigDecimal.ZERO) != 0;

                            GetParticipantPositionsDataByParticipantNameAndCurrencyQuery.Output
                                    participantPositionData =
                                    this.participantPositionsDataByParticipantNameAndCurrencyQuery.execute(new GetParticipantPositionsDataByParticipantNameAndCurrencyQuery.Input(
                                            hubParticipantDetailData.getParticipantName(),
                                            account.getNetSettlementAmount().getCurrency().toString()));

                            String positionCurrencyId = "0";
                            String settlementCurrencyId = "0";

                            ApprovalRequestData approvalRequestData = new ApprovalRequestData();
                            approvalRequestData.setAmount(new BigDecimal(account.getNetSettlementAmount()
                                                                                .getAmount()));
                            approvalRequestData.setCurrency(account.getNetSettlementAmount()
                                                                   .getCurrency()
                                                                   .toString());
                            approvalRequestData.setParticipantName(hubParticipantDetailData.getParticipantName());
                            approvalRequestData.setFundInOutAction(
                                PositionActionType.UPDATE_NDC_PERCENTAGE.toString());

                            if (participantPositionData != null) {

                                var list = participantPositionData.getParticipantPositionData();

                                if (list != null && !list.isEmpty() && list.get(0) != null) {

                                    var first = list.get(0);

                                    if (first.participantPositionCurrencyId() != null) {

                                        positionCurrencyId = first.participantPositionCurrencyId().toString();
                                    }
                                    if (first.participantSettlementCurrencyId() != null) {

                                        settlementCurrencyId = first.participantSettlementCurrencyId().toString();
                                    }
                                }
                            }

                            approvalRequestData.setParticipantPositionCurrencyId(positionCurrencyId);
                            approvalRequestData.setParticipantSettlementCurrencyId(settlementCurrencyId);

                            if (toRecalculateNDC) {
                                this.handleUpdateNdc.handleUpdateNdc(toRecalculateNDC,
                                                                     approvalRequestData,
                                                                     hubParticipantDetailData.getParticipantName(),
                                                                     account.getNetSettlementAmount()
                                                                            .getCurrency()
                                                                            .toString(),
                                                                     PositionActionType.valueOf(
                                                                         settlementAction.toString()));
                            }
                        }
                    }

                }

                LOG.info("Successfully settled net amounts to participants for settlementId: [{}].",
                         settlement.getId());
            }

            return new Output(true);
        } finally {
            this.isSettlementFinalized.set(false);
        }
    }

}
