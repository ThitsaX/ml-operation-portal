package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.SettlementHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCreateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.api.PutUpdateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowInfoData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetNetTransferAmountBySettlementIdQuery;
import com.thitsaworks.operation_portal.core.hub_services.support.Settlement;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAccount;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementParticipant;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementState;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateSettlement;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetNetTransferAmountBySettlementId;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreateSettlementHandler
    extends OperationPortalAuditableUseCase<CreateSettlement.Input, CreateSettlement.Output>
    implements CreateSettlement {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSettlementHandler.class);

    private final SettlementHubClient settlementHubClient;

    private final GetNetTransferAmountBySettlementIdQuery getNetTransferAmountBySettlementIdQuery;

    @Autowired
    public CreateSettlementHandler(CreateInputAuditCommand createInputAuditCommand,
                                   CreateOutputAuditCommand createOutputAuditCommand,
                                   CreateExceptionAuditCommand createExceptionAuditCommand,
                                   ObjectMapper objectMapper,
                                   PrincipalCache principalCache,
                                   ActionAuthorizationManager actionAuthorizationManager,
                                   SettlementHubClient settlementHubClient,
                                   GetNetTransferAmountBySettlementIdQuery getNetTransferAmountBySettlementIdQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.settlementHubClient = settlementHubClient;
        this.getNetTransferAmountBySettlementIdQuery = getNetTransferAmountBySettlementIdQuery;
    }

    @Override
    public Output onExecute(Input input) throws DomainException, ConnectException {

        PostCreateSettlement.Response response =
            this.settlementHubClient.createSettlement(new PostCreateSettlement.Request(input.settlementModel(),
                                                                                       input.reason(),
                                                                                       input.settlementWindowIdList()));

        GetNetTransferAmountBySettlementIdQuery.Output
            output =
            this.getNetTransferAmountBySettlementIdQuery.execute(new GetNetTransferAmountBySettlementIdQuery.Input(
                response.id()));

        List<GetNetTransferAmountBySettlementId.Detail> details = new ArrayList<>();

        for (SettlementWindowInfoData windowInfo : output.getWindowInfoList()) {

            if (windowInfo.getCredit() != null && windowInfo.getCredit()
                                                            .abs()
                                                            .compareTo(windowInfo.getParticipantBalance()
                                                                                 .abs()) > 0) {

                throw new ParticipantException(ParticipantErrors.ORG_INSUFFICIENT_BALANCE.format(windowInfo.getDfspName()));
            }

            Settlement settlement = this.settlementHubClient.getSettlement(response.id());

            PutUpdateSettlement.Request request = new PutUpdateSettlement.Request(settlement.getParticipants());

            List<SettlementParticipant> settlementParticipants = request.participants();

            SettlementState settlementState = SettlementState.valueOf(settlement.getState());

            while (!settlementParticipants.getFirst()
                                          .getAccounts()
                                          .getFirst()
                                          .getState()
                                          .equals(SettlementState.PS_TRANSFERS_RESERVED.toString())) {

                switch (settlementState) {
                    case PENDING_SETTLEMENT -> settlementState = SettlementState.PS_TRANSFERS_RECORDED;
                    case PS_TRANSFERS_RECORDED -> settlementState = SettlementState.PS_TRANSFERS_RESERVED;
                }

                for (SettlementParticipant participant : settlementParticipants) {
                    for (SettlementAccount account : participant.getAccounts()) {
                        account.setState(settlementState.toString());
                    }
                }

                PutUpdateSettlement.Response putUpdateSettlementResponse = this.settlementHubClient.putUpdateSettlement(
                    settlement.getId(),
                    new PutUpdateSettlement.Request(settlementParticipants));

                settlementState = SettlementState.valueOf(putUpdateSettlementResponse.state());

            }

        }
        return new Output(response.id(),
                          response.settlementModel(),
                          response.state(),
                          response.reason(),
                          response.createdDate(),
                          response.changedDate(),
                          response.settlementWindows(),
                          response.participants());
    }

}
