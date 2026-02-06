package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.ApprovalActionType;
import com.thitsaworks.operation_portal.component.common.type.PositionActionType;
import com.thitsaworks.operation_portal.component.fspiop.model.Currency;
import com.thitsaworks.operation_portal.component.fspiop.model.Extension;
import com.thitsaworks.operation_portal.component.fspiop.model.ExtensionList;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.TransferIdGenerator;
import com.thitsaworks.operation_portal.core.approval.command.ModifyApprovalActionCommand;
import com.thitsaworks.operation_portal.core.approval.exception.ApprovalException;
import com.thitsaworks.operation_portal.core.approval.query.ApprovalRequestQuery;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.ParticipantHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantBalanceByCurrencyIdQuery;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantLimitByCurrencyIdQuery;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAction;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantNDCQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyApprovalAction;
import com.thitsaworks.operation_portal.usecase.util.HandleUpdateNdc;
import com.thitsaworks.operation_portal.usecase.util.Utility;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.util.Locale;

@Service
public class ModifyApprovalActionHandler
    extends OperationPortalAuditableUseCase<ModifyApprovalAction.Input, ModifyApprovalAction.Output>
    implements ModifyApprovalAction {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyApprovalActionHandler.class);

    private final ObjectMapper objectMapper;

    private final ModifyApprovalActionCommand modifyApprovalActionCommand;

    private final ApprovalRequestQuery approvalRequestQuery;

    private final ParticipantHubClient participantHubClient;

    private final Utility utility;

    private final ParticipantNDCQuery participantNDCQuery;

    private final GetParticipantBalanceByCurrencyIdQuery getParticipantValueByCurrencyIdQuery;

    private final GetParticipantLimitByCurrencyIdQuery getParticipantLimitByCurrencyIdQuery;

    private final HandleUpdateNdc handleUpdateNdc;

    public ModifyApprovalActionHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       ActionAuthorizationManager actionAuthorizationManager,
                                       ModifyApprovalActionCommand modifyApprovalActionCommand,
                                       ApprovalRequestQuery approvalRequestQuery,
                                       ParticipantHubClient participantHubClient,
                                       Utility utility,
                                       ParticipantNDCQuery participantNDCQuery,
                                       GetParticipantBalanceByCurrencyIdQuery getParticipantValueByCurrencyIdQuery,
                                       GetParticipantLimitByCurrencyIdQuery getParticipantLimitByCurrencyIdQuery,
                                       HandleUpdateNdc handleUpdateNdc) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.modifyApprovalActionCommand = modifyApprovalActionCommand;
        this.approvalRequestQuery = approvalRequestQuery;
        this.participantHubClient = participantHubClient;
        this.utility = utility;
        this.participantNDCQuery = participantNDCQuery;
        this.getParticipantValueByCurrencyIdQuery = getParticipantValueByCurrencyIdQuery;
        this.getParticipantLimitByCurrencyIdQuery = getParticipantLimitByCurrencyIdQuery;
        this.handleUpdateNdc = handleUpdateNdc;
        this.objectMapper = objectMapper;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException, JsonProcessingException {

        LOG.info("Get Pending Approval Request by Id Query Request : approvalRequestId : {}",
                 input.approvalRequestId());

        var approvalRequestData = this.approvalRequestQuery.getPendingApprovalRequestByID(input.approvalRequestId());

        LOG.info("Get Pending Approval Response By Id Query Response : {}",
                 this.objectMapper.writeValueAsString(approvalRequestData));

        if (this.isSelfApprovalAttempt(approvalRequestData.getRequestedBy(), input.responseUserId())) {
            throw new IAMException(IAMErrors.SELF_APPROVAL_NOT_ALLOWED);
        }

        ModifyApprovalActionCommand.Output output;

        if (input.action()
                 .equals(ApprovalActionType.REJECTED)) {

            output = this.executeApprovalAction(input);

            return new Output(output.approvalRequestId());
        }

        final String fundInOutAction = approvalRequestData.getFundInOutAction();
        final PositionActionType actionType = parsePositionAction(fundInOutAction);

        final String action = switch (actionType) {
            case DEPOSIT -> SettlementAction.recordFundsIn.name();
            case WITHDRAW -> SettlementAction.recordFundsOutPrepareReserve.name();
            case UPDATE_NDC_FIXED -> SettlementAction.NET_DEBIT_CAP.name();
            case UPDATE_NDC_PERCENTAGE -> SettlementAction.NET_DEBIT_CAP.name();
            default -> fundInOutAction;
        };

        final String participantName = approvalRequestData.getParticipantName();
        final String currency = approvalRequestData.getCurrency();

        Money
            money =
            new Money().currency(Currency.valueOf(approvalRequestData.getCurrency()))
                       .amount(approvalRequestData.getAmount()
                                                  .toString());

        ExtensionList extensionList = new ExtensionList();
        Extension extension = new Extension();
        extension.setKey("requestUser");
        extension.setValue(this.utility.getEmail(new UserId(approvalRequestData.getRequestedBy()
                                                                               .getId())));
        extension.setKey("approveUser");
        extension.setValue(this.utility.getEmail(new UserId(input.responseUserId()
                                                                 .getId())));
        extensionList.addExtensionItem(extension);
        boolean toRecalculateNDC = false;

        if (actionType == PositionActionType.UPDATE_NDC_FIXED) {

            LOG.info(
                "Handle Update NDC Request : toRecalculateNDC : {}, approvalRequestData : {}, participantName : {}, currency : {}, actionType : {}",
                toRecalculateNDC,
                this.objectMapper.writeValueAsString(approvalRequestData),
                participantName,
                currency,
                actionType);

            this.handleUpdateNdc.handleUpdateNdc(toRecalculateNDC,
                                                 approvalRequestData,
                                                 participantName,
                                                 currency,
                                                 actionType,
                                                 false);

        } else if (actionType == PositionActionType.UPDATE_NDC_PERCENTAGE) {
            toRecalculateNDC = true;

            LOG.info(
                "Handle Update NDC Request : toRecalculateNDC : {}, approvalRequestData : {}, participantName : {}, currency : {}, actionType : {}",
                toRecalculateNDC,
                this.objectMapper.writeValueAsString(approvalRequestData),
                participantName,
                currency,
                actionType);

            this.handleUpdateNdc.handleUpdateNdc(toRecalculateNDC,
                                                 approvalRequestData,
                                                 participantName,
                                                 currency,
                                                 actionType,
                                                 false);

        } else {

            if (actionType == PositionActionType.WITHDRAW) {

                int
                    participantSettlementCurrencyId =
                    Integer.parseInt(approvalRequestData.getParticipantSettlementCurrencyId());
                int
                    participantPositionCurrencyId =
                    Integer.parseInt(approvalRequestData.getParticipantPositionCurrencyId());

                var
                    participantLimitInfo =
                    this.getParticipantLimitByCurrencyIdQuery.execute(new GetParticipantLimitByCurrencyIdQuery.Input(
                        approvalRequestData.getParticipantName(),
                        approvalRequestData.getCurrency()));

                var
                    participantBalanceInfo =
                    this.getParticipantValueByCurrencyIdQuery.execute(new GetParticipantBalanceByCurrencyIdQuery.Input(
                        participantSettlementCurrencyId));

                var
                    participantPositionInfo =
                    this.getParticipantValueByCurrencyIdQuery.execute(new GetParticipantBalanceByCurrencyIdQuery.Input(
                        participantPositionCurrencyId));

                if (participantLimitInfo == null || participantLimitInfo.getParticipantLimitData() == null) {

                    throw new ParticipantException(ParticipantErrors.PARTICIPANT_NDC_NOT_FOUND);

                }

                BigDecimal
                    currentBalance =
                    participantBalanceInfo.getParticipantBalanceData()
                                          .value()
                                          .abs();

                BigDecimal
                    currentParticipantLimit =
                    participantLimitInfo.getParticipantLimitData()
                                        .value();

                BigDecimal withdrawalAmount = approvalRequestData.getAmount();

                if (withdrawalAmount.compareTo(currentBalance) > 0) {
                    throw new ParticipantException(ParticipantErrors.INSUFFICIENT_BALANCE);
                }

                LOG.info("Get ParticipantNDC Query Request : participantName : {}, currency : {}",
                         participantName,
                         currency);

                var ndcData = this.participantNDCQuery.get(participantName, currency);

                LOG.info("Get ParticipantNDC Query Response : {}", ndcData);

                BigDecimal
                    ndcPercent = ndcData.map(ParticipantNDC::getNdcPercent)
                                        .orElse(BigDecimal.ZERO)
                                        .setScale(2, RoundingMode.DOWN);

                BigDecimal
                    participantPosition =
                    participantPositionInfo.getParticipantBalanceData()
                                           .value();

                // If NDC is a fixed amount (ndcPercent is 0), check remaining balance against NDC
                BigDecimal remainingBalance = currentBalance.subtract(withdrawalAmount);
                if (ndcPercent.compareTo(BigDecimal.ZERO) == 0) {

                    if (remainingBalance.compareTo(currentParticipantLimit) < 0) {
                        throw new ParticipantException(ParticipantErrors.BALANCE_BELOW_NDC);
                    }
                }
                if (participantPosition.compareTo(BigDecimal.ZERO) > 0) {

                    if (remainingBalance.compareTo(participantPosition.abs()) < 0) {
                        throw new ParticipantException(ParticipantErrors.BALANCE_BELOW_CURRENT_POSITION);
                    }
                }
            }

            String reason = actionType == PositionActionType.DEPOSIT ? "Deposit" :
                                "Withdrawal";

            PostParticipantBalance.Request
                request =
                new PostParticipantBalance.Request(TransferIdGenerator.generateTransferId(),
                                                   this.utility.getEmail(new UserId(approvalRequestData.getRequestedBy()
                                                                                                       .getId())),
                                                   action,
                                                   reason,
                                                   money,
                                                   extensionList);

            LOG.info("Get ParticipantNDC Query Request : participantName : {}, currency : {}",
                     participantName,
                     currency);

            var ndcData = this.participantNDCQuery.get(participantName, currency);

            LOG.info("Get ParticipantNDC Query Response : {}", ndcData);

            BigDecimal
                ndcPercent =
                ndcData.map(ParticipantNDC::getNdcPercent)
                       .orElse(BigDecimal.ZERO);

            toRecalculateNDC = ndcPercent.compareTo(BigDecimal.ZERO) != 0;

            LOG.info(
                "Post Participant Balance Request from op to mojaloop : participantName : {}, participantSettlementCurrencyId : {}, request : {}",
                approvalRequestData.getParticipantName(),
                approvalRequestData.getParticipantSettlementCurrencyId(),
                this.objectMapper.writeValueAsString(request));

            PostParticipantBalance.Response response = this.participantHubClient.postParticipantBalance(
                approvalRequestData.getParticipantName(),
                approvalRequestData.getParticipantSettlementCurrencyId(),
                request);

            LOG.info(
                "Post Participant Balance Response from mojaloop to op : {}",
                this.objectMapper.writeValueAsString(response));

            if (toRecalculateNDC) {

                LOG.info(
                    "Handle Update NDC Request : toRecalculateNDC : {}, approvalRequestData : {}, participantName : {}, currency : {}, actionType : {}",
                    toRecalculateNDC,
                    this.objectMapper.writeValueAsString(approvalRequestData),
                    participantName,
                    currency,
                    actionType);

                this.handleUpdateNdc.handleUpdateNdc(toRecalculateNDC,
                                                     approvalRequestData,
                                                     participantName,
                                                     currency,
                                                     actionType,
                                                     false);
            }

        }

        output = this.executeApprovalAction(input);

        return new Output(output.approvalRequestId());
    }

    private static PositionActionType parsePositionAction(String value) {

        if (value == null) {
            return null;
        }

        try {

            return PositionActionType.valueOf(value.trim()
                                                   .toUpperCase(Locale.ROOT));

        } catch (IllegalArgumentException ex) {

            return null;
        }
    }

    private boolean isSelfApprovalAttempt(UserId requestedByUserId, UserId respondedByUserId) {

        return requestedByUserId.getId()
                                .equals(respondedByUserId.getId());
    }

    private ModifyApprovalActionCommand.Output executeApprovalAction(Input input) throws ApprovalException {

        return this.modifyApprovalActionCommand.execute(new ModifyApprovalActionCommand.Input(input.approvalRequestId(),
                                                                                              input.action(),
                                                                                              input.responseUserId()));
    }

}
