package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.ParticipantHubClient;
import com.thitsaworks.operation_portal.core.hub_services.SettlementHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.GetSettlement;
import com.thitsaworks.operation_portal.core.hub_services.api.PostUpdateSettlementByParticipant;
import com.thitsaworks.operation_portal.core.hub_services.api.PutUpdateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantDetailData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.HubParticipantQuery;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAccount;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAction;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementLedgerAccountTypes;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementParticipant;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementState;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.FinalizeSettlement;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;
import java.util.UUID;

@Service
public class FinalizeSettlementHandler
        extends OperationPortalAuditableUseCase<FinalizeSettlement.Input, FinalizeSettlement.Output>
        implements FinalizeSettlement {

    private static final Logger LOG = LoggerFactory.getLogger(FinalizeSettlementHandler.class);

    private final SettlementHubClient settlementHubClient;

    private final ParticipantHubClient participantHubClient;

    private final HubParticipantQuery hubParticipantQuery;

    @Autowired
    public FinalizeSettlementHandler(CreateInputAuditCommand createInputAuditCommand,
                                     CreateOutputAuditCommand createOutputAuditCommand,
                                     CreateExceptionAuditCommand createExceptionAuditCommand,
                                     ObjectMapper objectMapper,
                                     PrincipalCache principalCache,
                                     ActionAuthorizationManager actionAuthorizationManager,
                                     SettlementHubClient settlementHubClient,
                                     ParticipantHubClient participantHubClient,
                                     HubParticipantQuery hubParticipantQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.settlementHubClient = settlementHubClient;
        this.participantHubClient = participantHubClient;
        this.hubParticipantQuery = hubParticipantQuery;
    }

    @Override
    public Output onExecute(Input input) throws DomainException, ConnectException {

        GetSettlement.Response settlementOutput = this.settlementHubClient.getSettlement(input.settlementId(),
                                                                                         new GetSettlement.Request());

        PutUpdateSettlement.Request request = new PutUpdateSettlement.Request(settlementOutput.participants());

        List<SettlementParticipant> settlementParticipants = request.participants();

        SettlementState settlementState = SettlementState.valueOf(settlementOutput.state());

        // call putUpdateSettlement until the settlementState is settled.
        while (!settlementParticipants.getFirst()
                                      .accounts()
                                      .getFirst()
                                      .getState()
                                      .equals(SettlementState.SETTLED.toString())) {

            switch (settlementState) {
                case PENDING_SETTLEMENT -> settlementState = SettlementState.PS_TRANSFERS_RECORDED;
                case PS_TRANSFERS_RECORDED -> settlementState = SettlementState.PS_TRANSFERS_RESERVED;
                case PS_TRANSFERS_RESERVED -> settlementState = SettlementState.PS_TRANSFERS_COMMITTED;
                case PS_TRANSFERS_COMMITTED -> settlementState = SettlementState.SETTLED;
            }

            for (SettlementParticipant participant : settlementParticipants) {
                for (SettlementAccount account : participant.accounts()) {
                    account.setState(settlementState.toString());
                }
            }

            PutUpdateSettlement.Response putUpdateSettlementResponse = this.settlementHubClient.putUpdateSettlement(
                    settlementOutput.id(),
                    new PutUpdateSettlement.Request(settlementParticipants));

            settlementState = SettlementState.valueOf(putUpdateSettlementResponse.state());

        }

        // call postUpdateSettlementByParticipant for all participant accounts
        List<HubParticipantDetailData> hubParticipantDetailDataList =
                this.hubParticipantQuery.getHubParticipantDetailDataList();

        for (SettlementParticipant participant : settlementParticipants) {

            for (SettlementAccount account : participant.accounts()) {

                String externalReference = "BOP settlement ID: " + settlementOutput.id();

                String reason = "Business Operations Portal settlement ID: " + settlementOutput.id() +
                        " finalization report processing";

                //  TODO: to confirm
                // if net amount is positive -> sender
                // if net amount is negative -> receiver
                SettlementAction settlementAction =
                        account.getNetSettlementAmount().getAmount().signum() > 0 ?
                                SettlementAction.recordFundsOutPrepareReserve :
                                SettlementAction.recordFundsIn;

                account.getNetSettlementAmount().setAmount(account.getNetSettlementAmount().getAmount().abs());

                HubParticipantDetailData hubParticipantDetailData =
                        hubParticipantDetailDataList.stream()
                                                    .filter(participantDetailData -> participantDetailData.getParticipantId()
                                                                                                          .equals(participant.id()))
                                                    .findFirst().orElse(null);

                if (hubParticipantDetailData == null) {
                    throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_NOT_FOUND);
                }

                Integer settleAccountId = hubParticipantDetailData.getAccounts().stream()
                                                                  .filter(acc -> acc.getCurrencyId()
                                                                                    .equals(account.getNetSettlementAmount()
                                                                                                   .getCurrency()) &&
                                                                          acc.getLedgerAccountTypeName().equals(
                                                                                  SettlementLedgerAccountTypes.SETTLEMENT.toString()))
                                                                  .map(HubParticipantDetailData.AccountData::getParticipantCurrencyId)
                                                                  .findFirst()
                                                                  .orElse(null);

                if (settleAccountId == null) {
                    throw new HubServicesException(HubServicesErrors.SETTLEMENT_ACCOUNT_NOT_FOUND);
                }

                PostUpdateSettlementByParticipant.Request postParticipantBalanceRequest =
                        new PostUpdateSettlementByParticipant.Request(UUID.randomUUID().toString(),
                                                                      settlementAction.toString(),
                                                                      reason,
                                                                      externalReference,
                                                                      account.getNetSettlementAmount());

                this.participantHubClient.postUpdateSettlementByParticipant(hubParticipantDetailData.getParticipantName(),
                                                                            settleAccountId,
                                                                            postParticipantBalanceRequest);
            }

        }

        return new Output(true);
    }

}
