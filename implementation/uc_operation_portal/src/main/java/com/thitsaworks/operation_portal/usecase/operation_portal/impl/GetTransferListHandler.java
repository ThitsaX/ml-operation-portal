package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.query.GetTransfersQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferData;

import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetTransferList;
import com.thitsaworks.operation_portal.usecase.util.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetTransferListHandler extends OperationPortalAuditableUseCase<GetTransferList.Input, GetTransferList.Output>
    implements GetTransferList {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferListHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION);

    private final GetTransfersQuery getTransfersQuery;

    private final ParticipantCache participantCache;

    private final ParticipantUserCache participantUserCache;

    public GetTransferListHandler(CreateInputAuditCommand createInputAuditCommand,
                                  CreateOutputAuditCommand createOutputAuditCommand,
                                  CreateExceptionAuditCommand createExceptionAuditCommand,
                                  ObjectMapper objectMapper,
                                  PrincipalCache principalCache,
                                  ActionAuthorizationManager actionAuthorizationManager,
                                  GetTransfersQuery getTransfersQuery,
                                  ParticipantCache participantCache,
                                  ParticipantUserCache participantUserCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.getTransfersQuery = getTransfersQuery;
        this.participantCache = participantCache;
        this.participantUserCache = participantUserCache;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        ParticipantUserData participantUserData = this.participantUserCache.get(input.participantUserId());

        if (participantUserData == null) {

            throw new ParticipantException(ParticipantErrors.USER_NOT_FOUND);
        }

        ParticipantData participantData = this.participantCache.get(participantUserData.participantId());

        if (participantData == null) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND);
        }

        String
            fspName =
            participantData.participantName()
                           .getValue();

        GetTransfersQuery.Output output = this.getTransfersQuery.execute(new GetTransfersQuery.Input(input.fromDate(),
                                                                                                     input.toDate(),
                                                                                                     input.transferId(),
                                                                                                     input.payerFspId(),
                                                                                                     input.payeeFspId(),
                                                                                                     input.payerIdentifierTypeId(),
                                                                                                     input.payeeIdentifierTypeId(),
                                                                                                     input.payerIdentifierValue(),
                                                                                                     input.payeeIdentifierValue(),
                                                                                                     input.currencyId(),
                                                                                                     input.transferStateId(),
                                                                                                     fspName,
                                                                                                     input.timeZone()));

        List<TransferData> transferDataList = new ArrayList<>();

        for (TransferData data : output.getTransferInfoList()) {

            transferDataList.add(
                new TransferData(
                    data.getTransferId(),
                    data.getState(),
                    data.getType(),
                    data.getCurrency(),
                    data.getAmount(),
                    data.getPayerDfsp(),
                    data.getPayeeDfsp(),
                    data.getWindowId(),
                    data.getSettlementBatch(),
                    data.getSubmittedOnDate()));
        }

        return new GetTransferList.Output(transferDataList);

    }

}
