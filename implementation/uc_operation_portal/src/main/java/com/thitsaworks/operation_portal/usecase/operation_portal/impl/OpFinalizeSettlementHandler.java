package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.PositionActionType;
import com.thitsaworks.operation_portal.component.fspiop.model.Currency;
import com.thitsaworks.operation_portal.component.fspiop.model.Extension;
import com.thitsaworks.operation_portal.component.fspiop.model.ExtensionList;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;
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
import com.thitsaworks.operation_portal.core.hub_services.query.GetNetTransferAmountBySettlementIdQuery;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantPositionsDataByParticipantNameAndCurrencyQuery;
import com.thitsaworks.operation_portal.core.hub_services.query.HubParticipantQuery;
import com.thitsaworks.operation_portal.core.hub_services.support.Settlement;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAccount;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAction;
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
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.Utility;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Primary
public class OpFinalizeSettlementHandler
    extends OperationPortalAuditableUseCase<FinalizeSettlement.Input, FinalizeSettlement.Output>
    implements FinalizeSettlement {

    private static final Logger LOG = LoggerFactory.getLogger(FinalizeSettlementHandler.class);

    private final ObjectMapper objectMapper;

    private final SettlementHubClient settlementHubClient;

    private final ParticipantHubClient participantHubClient;

    private final HubParticipantQuery hubParticipantQuery;

    private final GetNetTransferAmountBySettlementIdQuery getNetTransferAmountBySettlementIdQuery;

    private final AtomicBoolean isSettlementFinalized = new AtomicBoolean(false);

    private final ParticipantNDCQuery participantNDCQuery;

    private final HandleUpdateNdc handleUpdateNdc;

    private final GetParticipantPositionsDataByParticipantNameAndCurrencyQuery
        participantPositionsDataByParticipantNameAndCurrencyQuery;

    private final UserPermissionManager userPermissionManager;

    private final Utility utility;

    @Autowired
    public OpFinalizeSettlementHandler(CreateInputAuditCommand createInputAuditCommand,
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
                                       GetParticipantPositionsDataByParticipantNameAndCurrencyQuery participantPositionsDataByParticipantNameAndCurrencyQuery,
                                       UserPermissionManager userPermissionManager, Utility utility
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
        this.userPermissionManager = userPermissionManager;
        this.utility = utility;
        this.objectMapper = objectMapper;
    }

    @Override
    public Output onExecute(Input input) throws DomainException, ConnectException, JsonProcessingException {

        try {

            {
                var currentUser = this.userPermissionManager.getCurrentUser();

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
                        windowInfo.getNdcPercent(),
                        windowInfo.getDebit(),
                        windowInfo.getCredit(),
                        windowInfo.getCurrencyId(),
                        windowInfo.getParticipantSettlementCurrencyId());

                    details.add(detail);
                }

                LOG.info("Get Settlement Request from op to mojaloop : settlementId : {}", input.settlementId());

                Settlement settlement = this.settlementHubClient.getSettlement(input.settlementId());

                LOG.info("Get Settlement Response from mojaloop to op : {}",
                         this.objectMapper.writeValueAsString(settlement));

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

                    LOG.info("Put Update Settlement Request from op to mojaloop : settlementId : {}, request : {}",
                             settlement.getId(),
                             this.objectMapper.writeValueAsString(new PutUpdateSettlement.Request(
                                 settlementParticipants)));

                    PutUpdateSettlement.Response
                        putUpdateSettlementResponse =
                        this.settlementHubClient.putUpdateSettlement(settlement.getId(),
                                                                     new PutUpdateSettlement.Request(
                                                                         settlementParticipants));

                    LOG.info("Put Update Settlement Response from mojaloop to op : {}",
                             this.objectMapper.writeValueAsString(putUpdateSettlementResponse));

                    settlementState = SettlementState.valueOf(putUpdateSettlementResponse.state());

                }

                LOG.info(
                    "Updating settlement state for settlementId: [{}] is finished. Now calling postParticipantBalance apis for related participant accounts.",
                    settlement.getId());

                if (SettlementState.SETTLED.equals(settlementState)) {

                    LOG.info("Get Hub Participant Detail Data List Query Request : [no param]");

                    // call postParticipantBalance for participant accounts
                    List<HubParticipantDetailData>
                        hubParticipantDetailDataList =
                        this.hubParticipantQuery.getHubParticipantDetailDataList();

                    LOG.info("Get Hub Participant Detail Data List Query Response : {}",
                             this.objectMapper.writeValueAsString(hubParticipantDetailDataList));

                    ExtensionList extensionList = new ExtensionList();
                    extensionList.addExtensionItem(new Extension().key("settlementId")
                                                                  .value(settlement.getId()
                                                                                   .toString()));
                    extensionList.addExtensionItem(new Extension().key("approveUser") //actually it is login user but in order to see at report
                                                                  .value(this.utility.getEmail(new UserId(currentUser.principalId()
                                                                                                                     .getId()))));

                    for (GetNetTransferAmountBySettlementId.Detail detail : details) {

                        String externalReference = "BOP settlement ID: " + settlement.getId();

                        String
                            reason =
                            "Settlement:" + settlement.getId();

                        SettlementAction settlementAction = null;
                        PositionActionType positionActionType = null;
                        BigDecimal amount = BigDecimal.ZERO;

                        if (detail.debitAmount() != null && detail.debitAmount()
                                                                  .compareTo(BigDecimal.ZERO) > 0) {
                            settlementAction = SettlementAction.recordFundsIn;
                            positionActionType = PositionActionType.DEPOSIT;
                            amount = detail.debitAmount();

                        } else if (detail.creditAmount() != null && detail.creditAmount()
                                                                          .compareTo(BigDecimal.ZERO) > 0) {
                            settlementAction = SettlementAction.recordFundsOutPrepareReserve;
                            positionActionType = PositionActionType.WITHDRAW;
                            amount = detail.creditAmount();

                        } else {

                            LOG.info("Both Debit and Credit are zero or empty.");
                            break;
                        }

                        Money netAmount = new Money();
                        netAmount.setCurrency(Currency.valueOf(detail.currency()));
                        netAmount.setAmount(amount.abs()
                                                  .toString());

                        PostParticipantBalance.Request
                            postParticipantBalanceRequest =
                            new PostParticipantBalance.Request(UUID.randomUUID()
                                                                   .toString(),
                                                               externalReference,
                                                               settlementAction.toString(),
                                                               reason,
                                                               netAmount,
                                                               extensionList);

                        LOG.info(
                            "Post Participant Balance Request from op to mojaloop : participantName : {}, settleAccountId : {}, request : {}",
                            detail.participantName(),
                            detail.participantSettlementCurrencyId(),
                            this.objectMapper.writeValueAsString(postParticipantBalanceRequest));

                        this.participantHubClient.postParticipantBalance(detail.participantName(),
                                                                         detail.participantSettlementCurrencyId(),
                                                                         postParticipantBalanceRequest);

                        LOG.info("Post Participant Balance Response from mojaloop to op : [no response]");

                        LOG.info("Get ParticipantNDC Query Request : participantName : {}, currency : {}",
                                 detail.participantName(),
                                 detail.currency());

                        var ndcData = this.participantNDCQuery.get(detail.participantName(),
                                                                   detail.currency());

                        LOG.info("Get ParticipantNDC Query Response : {}", ndcData);

                        boolean toRecalculateNDC = false;

                        BigDecimal
                            ndcPercent =
                            ndcData.map(ParticipantNDC::getNdcPercent)
                                   .orElse(BigDecimal.ZERO);

                        toRecalculateNDC = ndcPercent.compareTo(BigDecimal.ZERO) != 0;

                        GetParticipantPositionsDataByParticipantNameAndCurrencyQuery.Output
                            participantPositionData =
                            this.participantPositionsDataByParticipantNameAndCurrencyQuery.execute(new GetParticipantPositionsDataByParticipantNameAndCurrencyQuery.Input(
                                detail.participantName(),
                                detail.currency()));

                        String positionCurrencyId = "0";
                        String settlementCurrencyId = "0";

                        ApprovalRequestData approvalRequestData = new ApprovalRequestData();
                        approvalRequestData.setAmount(new BigDecimal(netAmount.getAmount()));
                        approvalRequestData.setCurrency(netAmount.getCurrency()
                                                                 .toString());
                        approvalRequestData.setParticipantName(detail.participantName());
                        approvalRequestData.setFundInOutAction(
                            positionActionType.toString());

                        if (participantPositionData != null) {

                            var list = participantPositionData.getParticipantPositionData();

                            if (list != null && !list.isEmpty() && list.get(0) != null) {

                                var first = list.get(0);

                                if (first.participantPositionCurrencyId() != null) {

                                    positionCurrencyId =
                                        first.participantPositionCurrencyId()
                                             .toString();
                                }
                                if (first.participantSettlementCurrencyId() != null) {

                                    settlementCurrencyId =
                                        first.participantSettlementCurrencyId()
                                             .toString();
                                }
                            }
                        }

                        approvalRequestData.setParticipantPositionCurrencyId(positionCurrencyId);
                        approvalRequestData.setParticipantSettlementCurrencyId(settlementCurrencyId);

                        if (toRecalculateNDC) {

                            LOG.info(
                                "Handle Update NDC Request : toRecalculateNDC : {}, approvalRequestData : {}, participantName : {}, currency : {}, actionType : {}",
                                toRecalculateNDC,
                                this.objectMapper.writeValueAsString(approvalRequestData),
                                detail.participantName(),
                                detail.currency(),
                                positionActionType);

                            this.handleUpdateNdc.handleUpdateNdc(toRecalculateNDC,
                                                                 approvalRequestData,
                                                                 detail.participantName(),
                                                                 detail.currency(),
                                                                 positionActionType);
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

