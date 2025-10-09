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
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantBalanceByCurrencyIdQuery;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAction;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantNDCCommand;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantNDCCommand;
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

    private final ParticipantNDCQuery participantNDCQuery;

    private final GetParticipantBalanceByCurrencyIdQuery getParticipantBalanceByCurrencyIdQuery;

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
                                       ParticipantNDCQuery participantNDCQuery,
                                       GetParticipantBalanceByCurrencyIdQuery getParticipantBalanceByCurrencyIdQuery) {

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
        this.participantNDCQuery = participantNDCQuery;
        this.getParticipantBalanceByCurrencyIdQuery = getParticipantBalanceByCurrencyIdQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var approvalRequestData = this.approvalRequestQuery.getPendingApprovalRequestByID(input.approvalRequestId());

        if (this.isSelfApprovalAttempt(approvalRequestData.requestedBy(), input.responseUserId())) {
            throw new IAMException(IAMErrors.SELF_APPROVAL_NOT_ALLOWED);
        }

        ModifyApprovalActionCommand.Output output;

        if (input.action()
                 .equals(ApprovalActionType.REJECTED)) {

            output = this.executeApprovalAction(input);

            return new Output(output.approvalRequestId());
        }

        final String requested = approvalRequestData.requestedAction();
        final PositionActionType actionType = parsePositionAction(requested);

        final String action = switch (actionType) {
            case DEPOSIT -> SettlementAction.recordFundsIn.name();
            case WITHDRAW -> SettlementAction.recordFundsOutPrepareReserve.name();
            case UPDATE_NDC_FIXED -> SettlementAction.NET_DEBIT_CAP.name();
            case UPDATE_NDC_PERCENTAGE -> SettlementAction.NET_DEBIT_CAP.name();
            default -> requested;
        };

        final String participantName = approvalRequestData.participantName();
        final String currency = approvalRequestData.currency();

        Money money = new Money().currency(Currency.valueOf(approvalRequestData.currency()))
                                 .amount(approvalRequestData.amount()
                                                            .toString());

        ExtensionList extensionList = new ExtensionList();
        Extension extension = new Extension();
        extension.setKey("requestUser");
        extension.setValue(this.utility.getEmail(new UserId(approvalRequestData.requestedBy()
                                                                               .getId())));
        extension.setKey("approveUser");
        extension.setValue(this.utility.getEmail(new UserId(approvalRequestData.requestedBy()
                                                                               .getId())));
        extensionList.addExtensionItem(extension);

        if (actionType == PositionActionType.UPDATE_NDC_FIXED) {

            this.handleUpdateNdc(PositionActionType.UPDATE_NDC_FIXED, approvalRequestData, participantName, currency);

        } else if (actionType == PositionActionType.UPDATE_NDC_PERCENTAGE) {

            this.handleUpdateNdc(PositionActionType.UPDATE_NDC_PERCENTAGE,
                                 approvalRequestData,
                                 participantName,
                                 currency);

        } else {

            PostParticipantBalance.Request request =
                new PostParticipantBalance.Request(TransferIdGenerator.generateTransferId(),
                                                   this.utility.getEmail(new UserId(approvalRequestData.requestedBy()
                                                                                                       .getId())),
                                                   action,
                                                   "Admin portal funds in request",
                                                   money,
                                                   extensionList);

            PostParticipantBalance.Response
                response =
                this.participantHubClient.postParticipantBalance(approvalRequestData.participantName(),
                                                                 approvalRequestData.participantCurrencyId(),
                                                                 request);
        }

        output = this.executeApprovalAction(input);

        return new Output(output.approvalRequestId());
    }

    private static PositionActionType parsePositionAction(String value) {

        if (value == null) { return null; }

        try {

            return PositionActionType.valueOf(value.trim()
                                                   .toUpperCase(Locale.ROOT));

        } catch (IllegalArgumentException ex) {

            return null;
        }
    }

    private static BigDecimal computeNdcAmount(ApprovalRequestData req, GetParticipantBalanceByCurrencyIdQuery.Output out) {

        if (out == null) { return new BigDecimal(0); }

        var data = out.getParticipantBalanceData();
        if (data == null) { return new BigDecimal(0); }

        var amount = req.amount();
        if (amount == null || amount.signum() <= 0) { return new BigDecimal(0); }

        if (!Objects.equals(data.currency(), req.currency())) { return new BigDecimal(0); }
        if (!"SETTLEMENT".equalsIgnoreCase(String.valueOf(data.ledgerAccountType()))) { return new BigDecimal(0); }

        var
            balance =
            Optional.ofNullable(data.value())
                    .orElse(BigDecimal.ZERO)
                    .abs();

        try {

            return balance.multiply(amount)
                          .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP).abs();

        } catch (ArithmeticException ex) {

            return balance.multiply(amount)
                          .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP).abs();

        }
    }

    private void handleUpdateNdc(PositionActionType actionType,
                                 ApprovalRequestData approvalRequestData,
                                 String dfsp,
                                 String currency) throws
                                                  HubServicesException,
                                                  ParticipantException,
                                                  ParticipantNDCException {

        BigDecimal limitValue = switch (actionType) {

            case UPDATE_NDC_FIXED -> approvalRequestData.amount();
            case UPDATE_NDC_PERCENTAGE -> {

                int participantCurrencyId = Integer.parseInt(approvalRequestData.participantCurrencyId());
                var balanceInfo =
                    this.getParticipantBalanceByCurrencyIdQuery.execute(new GetParticipantBalanceByCurrencyIdQuery.Input(
                        participantCurrencyId));
                yield computeNdcAmount(approvalRequestData, balanceInfo);
            }
            default -> throw new IllegalArgumentException("Unsupported actionType: " + actionType);
        };

        var request = new PutUpdateParticipantLimit.Request(approvalRequestData.currency(),
                                                            new PutUpdateParticipantLimit.Limit(SettlementAction.NET_DEBIT_CAP.toString(),
                                                                                                limitValue,
                                                                                                10));

        this.participantHubClient.putUpdateParticipantLimit(approvalRequestData.participantName(), request);

        var optionalNdc = this.participantNDCQuery.get(dfsp, currency);

        if (optionalNdc.isEmpty()) {

            BigDecimal ndcAmount = (actionType == PositionActionType.UPDATE_NDC_FIXED) ? BigDecimal.ZERO :
                                       approvalRequestData.amount();

            this.createParticipantNDCCommand.execute(new CreateParticipantNDCCommand.Input(dfsp, currency, ndcAmount));

        } else {

            BigDecimal ndcAmount = (actionType == PositionActionType.UPDATE_NDC_FIXED) ? BigDecimal.ZERO :
                                       approvalRequestData.amount();

            this.modifyParticipantNDCCommand.execute(new ModifyParticipantNDCCommand.Input(optionalNdc.get()
                                                                                                      .participantNDCId(),
                                                                                           ndcAmount));
        }
    }

    private boolean isSelfApprovalAttempt(UserId requestedByUserId, UserId respondedByUserId) {

        return requestedByUserId.getId()
                                .equals(respondedByUserId.getId());
    }

    private ModifyApprovalActionCommand.Output executeApprovalAction(Input input)
        throws ApprovalException {

        return this.modifyApprovalActionCommand.execute(new ModifyApprovalActionCommand.Input(input.approvalRequestId(),
                                                                                              input.action(),
                                                                                              input.responseUserId()));
    }

}
