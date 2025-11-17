package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

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
import com.thitsaworks.operation_portal.core.approval.data.ApprovalRequestData;
import com.thitsaworks.operation_portal.core.approval.exception.ApprovalException;
import com.thitsaworks.operation_portal.core.approval.query.ApprovalRequestQuery;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.ParticipantHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import com.thitsaworks.operation_portal.core.hub_services.api.PutUpdateParticipantLimit;
import com.thitsaworks.operation_portal.core.hub_services.data.ParticipantBalanceData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantBalanceByCurrencyIdQuery;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantLimitByCurrencyIdQuery;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAction;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantNDCCommand;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantNDCHistoryCommand;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantNDCCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNDCException;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantNDCQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyApprovalAction;
import com.thitsaworks.operation_portal.usecase.util.Utility;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
public class ModifyApprovalActionHandler
    extends OperationPortalAuditableUseCase<ModifyApprovalAction.Input, ModifyApprovalAction.Output>
    implements ModifyApprovalAction {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyApprovalActionHandler.class);

    private final ModifyApprovalActionCommand modifyApprovalActionCommand;

    private final ApprovalRequestQuery approvalRequestQuery;

    private final ParticipantHubClient participantHubClient;

    private final Utility utility;

    private final CreateParticipantNDCCommand createParticipantNDCCommand;

    private final ModifyParticipantNDCCommand modifyParticipantNDCCommand;

    private final CreateParticipantNDCHistoryCommand createParticipantNDCHistoryCommand;

    private final ParticipantNDCQuery participantNDCQuery;

    private final GetParticipantBalanceByCurrencyIdQuery getParticipantBalanceByCurrencyIdQuery;

    private final GetParticipantLimitByCurrencyIdQuery getParticipantLimitByCurrencyIdQuery;

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
                                       CreateParticipantNDCCommand createParticipantNDCCommand,
                                       ModifyParticipantNDCCommand modifyParticipantNDCCommand,
                                       CreateParticipantNDCHistoryCommand createParticipantNDCHistoryCommand,
                                       ParticipantNDCQuery participantNDCQuery,
                                       GetParticipantBalanceByCurrencyIdQuery getParticipantBalanceByCurrencyIdQuery,
                                       GetParticipantLimitByCurrencyIdQuery getParticipantLimitByCurrencyIdQuery) {

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
        this.createParticipantNDCCommand = createParticipantNDCCommand;
        this.modifyParticipantNDCCommand = modifyParticipantNDCCommand;
        this.createParticipantNDCHistoryCommand = createParticipantNDCHistoryCommand;
        this.participantNDCQuery = participantNDCQuery;
        this.getParticipantBalanceByCurrencyIdQuery = getParticipantBalanceByCurrencyIdQuery;
        this.getParticipantLimitByCurrencyIdQuery = getParticipantLimitByCurrencyIdQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var approvalRequestData = this.approvalRequestQuery.getPendingApprovalRequestByID(input.approvalRequestId());

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
        extension.setValue(this.utility.getEmail(new UserId(approvalRequestData.getRequestedBy()
                                                                               .getId())));
        extensionList.addExtensionItem(extension);
        boolean toRecalculateNDC = false;

        if (actionType == PositionActionType.UPDATE_NDC_FIXED) {

            this.handleUpdateNdc(toRecalculateNDC, approvalRequestData, participantName, currency, actionType);

        } else if (actionType == PositionActionType.UPDATE_NDC_PERCENTAGE) {
            toRecalculateNDC = true;

            this.handleUpdateNdc(toRecalculateNDC, approvalRequestData, participantName, currency, actionType);

        } else {

            if (actionType == PositionActionType.WITHDRAW) {

                int participantCurrencyId = Integer.parseInt(approvalRequestData.getParticipantCurrencyId());
                var
                    participantLimitInfo =
                    this.getParticipantLimitByCurrencyIdQuery.execute(new GetParticipantLimitByCurrencyIdQuery.Input(
                        approvalRequestData.getParticipantName(),
                        approvalRequestData.getCurrency()));

                var
                    participantBalanceInfo =
                    this.getParticipantBalanceByCurrencyIdQuery.execute(new GetParticipantBalanceByCurrencyIdQuery.Input(
                        participantCurrencyId));

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
                    throw new ParticipantException(ParticipantErrors.INSUFFICIENT_BALANCE.format(withdrawalAmount));
                }

                var ndcData = this.participantNDCQuery.get(participantName, currency);
                if (ndcData.isPresent()) {
                    BigDecimal
                        ndcPercent =
                        ndcData.get()
                               .getNdcPercent();

                    // If NDC is a fixed amount (ndcPercent is 0), check remaining balance against NDC
                    if (ndcPercent.compareTo(BigDecimal.ZERO) == 0) {
                        BigDecimal remainingBalance = currentBalance.subtract(withdrawalAmount);
                        if (remainingBalance.compareTo(currentParticipantLimit) < 0) {
                            throw new ParticipantException(ParticipantErrors.NDC_LIMIT_EXCEEDED);
                        }
                    }
                }
            }

            String
                reason =
                actionType == PositionActionType.DEPOSIT ? "Admin portal funds in request" :
                    "Admin portal funds out request";

            PostParticipantBalance.Request
                request =
                new PostParticipantBalance.Request(TransferIdGenerator.generateTransferId(),
                                                   this.utility.getEmail(new UserId(approvalRequestData.getRequestedBy()
                                                                                                       .getId())),
                                                   action,
                                                   reason,
                                                   money,
                                                   extensionList);

            var ndcData = this.participantNDCQuery.get(participantName, currency);

            BigDecimal
                ndcPercent =
                ndcData.get()
                       .getNdcPercent();
            toRecalculateNDC = ndcPercent.compareTo(BigDecimal.ZERO) != 0;

            if (toRecalculateNDC) {
                this.handleUpdateNdc(toRecalculateNDC, approvalRequestData, participantName, currency, actionType);
            }

            PostParticipantBalance.Response response = this.participantHubClient.postParticipantBalance(
                approvalRequestData.getParticipantName(),
                approvalRequestData.getParticipantCurrencyId(),
                request);

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

    private BigDecimal computeNdcAmount(ApprovalRequestData req,
                                        GetParticipantBalanceByCurrencyIdQuery.Output balanceInfo) {

        if (balanceInfo == null) {
            return new BigDecimal(0);
        }

        var balanceData = balanceInfo.getParticipantBalanceData();
        if (balanceData == null) {
            return new BigDecimal(0);
        }
        var ndcPercent = req.getAmount();

        if (req.getFundInOutAction()
               .equalsIgnoreCase("WITHDRAW") || req.getFundInOutAction()
                                                   .equalsIgnoreCase("DEPOSIT")) {
            var ndcData = this.participantNDCQuery.get(req.getParticipantName(), req.getCurrency());

            ndcPercent = ndcData.get()
                                .getNdcPercent()
                                .setScale(2, RoundingMode.HALF_DOWN);
        }

        if (ndcPercent == null || ndcPercent.signum() <= 0) {
            return new BigDecimal(0);
        }

        if (!Objects.equals(balanceData.currency(), req.getCurrency())) {
            return new BigDecimal(0);
        }
        if (!"SETTLEMENT".equalsIgnoreCase(String.valueOf(balanceData.ledgerAccountType()))) {
            return new BigDecimal(0);
        }

        var
            balance =
            Optional.ofNullable(balanceData.value())
                    .orElse(BigDecimal.ZERO)
                    .abs();
        BigDecimal calculatedNdcValue;
        calculatedNdcValue = balance.multiply(ndcPercent)
                                    .divide(BigDecimal.valueOf(100))
                                    .abs()
                                    .setScale(2, RoundingMode.HALF_DOWN);

        return calculatedNdcValue;

    }

    private void handleUpdateNdc(Boolean ToCalculateNdc,
                                 ApprovalRequestData approvalRequestData,
                                 String participantName,
                                 String currency,
                                 PositionActionType actionType)
        throws HubServicesException, ParticipantException, ParticipantNDCException {

        BigDecimal calculatedNdcLimit;

        if (ToCalculateNdc) {
            int participantCurrencyId = Integer.parseInt(approvalRequestData.getParticipantCurrencyId());
            var
                balanceInfo =
                this.getParticipantBalanceByCurrencyIdQuery.execute(new GetParticipantBalanceByCurrencyIdQuery.Input(
                    participantCurrencyId));
            BigDecimal updatedBalance = balanceInfo.getParticipantBalanceData()
                                                   .value()
                                                   .abs();
            if (actionType == PositionActionType.DEPOSIT) {
                updatedBalance = updatedBalance.add(approvalRequestData.getAmount());
            } else if (actionType == PositionActionType.WITHDRAW) {
                updatedBalance = updatedBalance.subtract(approvalRequestData.getAmount());
            }

            ParticipantBalanceData balanceData = new ParticipantBalanceData(balanceInfo.getParticipantBalanceData()
                                                                                       .currency(),
                                                                            balanceInfo.getParticipantBalanceData()
                                                                                       .ledgerAccountType(),
                                                                            updatedBalance.setScale(2,
                                                                                                    RoundingMode.HALF_DOWN),
                                                                            balanceInfo.getParticipantBalanceData()
                                                                                       .reservedValue(),
                                                                            balanceInfo.getParticipantBalanceData()
                                                                                       .isActive(),
                                                                            balanceInfo.getParticipantBalanceData()
                                                                                       .changedDate());

            calculatedNdcLimit = computeNdcAmount(approvalRequestData,
                                                  new GetParticipantBalanceByCurrencyIdQuery.Output(balanceData));
        } else {
            calculatedNdcLimit = approvalRequestData.getAmount();
        }

        var request = new PutUpdateParticipantLimit.Request(approvalRequestData.getCurrency(),
                                                            new PutUpdateParticipantLimit.Limit(SettlementAction.NET_DEBIT_CAP.toString(),
                                                                                                calculatedNdcLimit,
                                                                                                10));

        this.participantHubClient.putUpdateParticipantLimit(approvalRequestData.getParticipantName(), request);

        var optionalNdc = this.participantNDCQuery.get(participantName, currency);

        if (optionalNdc.isEmpty()) {

            BigDecimal
                ndcAmount =
                (actionType == PositionActionType.UPDATE_NDC_FIXED) ? BigDecimal.ZERO : approvalRequestData.getAmount();

            this.createParticipantNDCCommand.execute(new CreateParticipantNDCCommand.Input(participantName,
                                                                                           currency,
                                                                                           ndcAmount));

        } else {

            BigDecimal ndcAmount;

            if (actionType == PositionActionType.UPDATE_NDC_FIXED) {
                ndcAmount = BigDecimal.ZERO;
            } else if (actionType == PositionActionType.WITHDRAW || actionType == PositionActionType.DEPOSIT) {
                // keep the current percent (no overwrite)
                ndcAmount =
                    optionalNdc.get()
                               .getNdcPercent();
            } else {
                ndcAmount = approvalRequestData.getAmount(); // other action types, if any
            }

            this.createParticipantNDCHistoryCommand.execute(
                new CreateParticipantNDCHistoryCommand.Input(optionalNdc.get())
                                                           );

            this.modifyParticipantNDCCommand.execute(
                new ModifyParticipantNDCCommand.Input(
                    optionalNdc.get()
                               .getParticipantNDCId(),
                    ndcAmount.setScale(2, RoundingMode.HALF_DOWN)
                )
                                                    );

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
