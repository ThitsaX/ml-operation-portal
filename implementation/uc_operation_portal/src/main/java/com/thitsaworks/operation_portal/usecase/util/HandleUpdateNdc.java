package com.thitsaworks.operation_portal.usecase.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.PositionActionType;
import com.thitsaworks.operation_portal.core.approval.data.ApprovalRequestData;
import com.thitsaworks.operation_portal.core.hub_services.ParticipantHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.PutUpdateParticipantLimit;
import com.thitsaworks.operation_portal.core.hub_services.data.ParticipantBalanceData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantBalanceByCurrencyIdQuery;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAction;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantNDCCommand;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantNDCHistoryCommand;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantNDCCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNDCException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantNDCQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

@Service
public class HandleUpdateNdc {

    private static final Logger LOG = LoggerFactory.getLogger(HandleUpdateNdc.class);

    private final ObjectMapper objectMapper;

    private final GetParticipantBalanceByCurrencyIdQuery getParticipantValueByCurrencyIdQuery;

    private final ParticipantNDCQuery participantNDCQuery;

    private final CreateParticipantNDCCommand createParticipantNDCCommand;

    private final ModifyParticipantNDCCommand modifyParticipantNDCCommand;

    private final CreateParticipantNDCHistoryCommand createParticipantNDCHistoryCommand;

    private final ParticipantHubClient participantHubClient;

    public HandleUpdateNdc(GetParticipantBalanceByCurrencyIdQuery getParticipantValueByCurrencyIdQuery,
                           ParticipantNDCQuery participantNDCQuery,
                           CreateParticipantNDCCommand createParticipantNDCCommand,
                           ModifyParticipantNDCCommand modifyParticipantNDCCommand,
                           CreateParticipantNDCHistoryCommand createParticipantNDCHistoryCommand,
                           ParticipantHubClient participantHubClient,
                           ObjectMapper objectMapper) {

        this.getParticipantValueByCurrencyIdQuery = getParticipantValueByCurrencyIdQuery;
        this.participantNDCQuery = participantNDCQuery;
        this.createParticipantNDCCommand = createParticipantNDCCommand;
        this.modifyParticipantNDCCommand = modifyParticipantNDCCommand;
        this.createParticipantNDCHistoryCommand = createParticipantNDCHistoryCommand;
        this.participantHubClient = participantHubClient;
        this.objectMapper = objectMapper;
    }

    public void handleUpdateNdc(Boolean ToCalculateNdc,
                                ApprovalRequestData approvalRequestData,
                                String participantName,
                                String currency,
                                PositionActionType actionType,
                                boolean balanceAlreadyUpdated)
        throws HubServicesException, ParticipantException, ParticipantNDCException, JsonProcessingException {

        BigDecimal calculatedNdcLimit;
        int
            participantSettlementCurrencyId =
            Integer.parseInt(approvalRequestData.getParticipantSettlementCurrencyId());
        var
            balanceInfo =
            this.getParticipantValueByCurrencyIdQuery.execute(new GetParticipantBalanceByCurrencyIdQuery.Input(
                participantSettlementCurrencyId));
        BigDecimal updatedBalance = balanceInfo.getParticipantBalanceData()
                                               .value()
                                               .abs();
        var
            positionInfo =
            this.getParticipantValueByCurrencyIdQuery.execute(new GetParticipantBalanceByCurrencyIdQuery.Input(
                Integer.parseInt(approvalRequestData.getParticipantPositionCurrencyId())));

        if (ToCalculateNdc) {

            if (!balanceAlreadyUpdated) {
                if (actionType == PositionActionType.DEPOSIT) {
                    updatedBalance = updatedBalance.add(approvalRequestData.getAmount());
                } else if (actionType == PositionActionType.WITHDRAW) {
                    updatedBalance = updatedBalance.subtract(approvalRequestData.getAmount());
                }
            }

            ParticipantBalanceData balanceData = new ParticipantBalanceData(balanceInfo.getParticipantBalanceData()
                                                                                       .currency(),
                                                                            balanceInfo.getParticipantBalanceData()
                                                                                       .ledgerAccountType(),
                                                                            updatedBalance.setScale(2,
                                                                                                    RoundingMode.DOWN),
                                                                            balanceInfo.getParticipantBalanceData()
                                                                                       .reservedValue(),
                                                                            balanceInfo.getParticipantBalanceData()
                                                                                       .isActive(),
                                                                            balanceInfo.getParticipantBalanceData()
                                                                                       .changedDate());

            LOG.info("Compute NDC Amount Request : approvalRequestData : {}, balanceData : {}",
                     this.objectMapper.writeValueAsString(approvalRequestData),
                     this.objectMapper.writeValueAsString(new GetParticipantBalanceByCurrencyIdQuery.Output(balanceData)));

            calculatedNdcLimit = computeNdcAmount(approvalRequestData,
                                                  new GetParticipantBalanceByCurrencyIdQuery.Output(balanceData));

            LOG.info("Compute NDC Amount Response : {}", calculatedNdcLimit);

        } else {

            if (approvalRequestData.getAmount()
                                   .compareTo(updatedBalance) > 0) {
                throw new ParticipantException(ParticipantErrors.NDC_BALANCE_EXCEEDED);
            }

            calculatedNdcLimit = approvalRequestData.getAmount();
        }

        if (positionInfo.getParticipantBalanceData()
                        .value()
                        .compareTo(BigDecimal.ZERO) > 0) {

            if (calculatedNdcLimit.compareTo(positionInfo.getParticipantBalanceData()
                                                         .value()
                                                         .abs()) < 0) {

                throw actionType == PositionActionType.WITHDRAW
                          ? new ParticipantException(ParticipantErrors.NDC_BELOW_CURRENT_POSITION)
                          : new ParticipantException(ParticipantErrors.NDC_LOWER_THAN_CURRENT_POSITION);

            }
        }

        var request = new PutUpdateParticipantLimit.Request(approvalRequestData.getCurrency(),
                                                            new PutUpdateParticipantLimit.Limit(SettlementAction.NET_DEBIT_CAP.toString(),
                                                                                                calculatedNdcLimit,
                                                                                                10));

        LOG.info("Put Update Participant Limit Request from op to mojaloop : participantName : {}, request : {}",
                 approvalRequestData.getParticipantName(),
                 this.objectMapper.writeValueAsString(request));

        this.participantHubClient.putUpdateParticipantLimit(approvalRequestData.getParticipantName(), request);

        LOG.info("Put Update Participant Limit Response from op to mojaloop : [no response]");

        LOG.info("Get ParticipantNDC Query Request : participantName : {}, currency : {}", participantName, currency);

        var optionalNdc = this.participantNDCQuery.get(participantName, currency);

        LOG.info("Get ParticipantNDC Query Response : {}", optionalNdc);

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

                LOG.info("NDC Amount : {} if actionType = {}", ndcAmount, actionType);

            } else if (actionType == PositionActionType.WITHDRAW || actionType == PositionActionType.DEPOSIT) {
                // keep the current percent (no overwrite)

                ndcAmount =
                    optionalNdc.get()
                               .getNdcPercent();

                LOG.info("NDC Amount : {} if actionType = {}", ndcAmount, actionType);

            } else {

                ndcAmount = approvalRequestData.getAmount(); // other action types, if any

                LOG.info("NDC Amount : {} if other actionType", ndcAmount);

            }

            this.createParticipantNDCHistoryCommand.execute(
                new CreateParticipantNDCHistoryCommand.Input(optionalNdc.get())
                                                           );

            this.modifyParticipantNDCCommand.execute(
                new ModifyParticipantNDCCommand.Input(
                    optionalNdc.get()
                               .getParticipantNDCId(),
                    ndcAmount.setScale(2, RoundingMode.DOWN)
                )
                                                    );

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

            LOG.info("Get ParticipantNDC Query Request : participantName : {}, currency : {}", req.getParticipantName(), req.getCurrency());

            var ndcData = this.participantNDCQuery.get(req.getParticipantName(), req.getCurrency());

            LOG.info("Get ParticipantNDC Query Response : {}", ndcData);


            ndcPercent = ndcData.map(ParticipantNDC::getNdcPercent)
                                .orElse(BigDecimal.ZERO)
                                .setScale(2, RoundingMode.DOWN);
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
                                    .setScale(2, RoundingMode.DOWN);

        return calculatedNdcValue;

    }

}
