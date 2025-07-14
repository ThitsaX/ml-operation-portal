package com.thitsaworks.operation_portal.usecase.hub_services.impl;

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

import com.thitsaworks.operation_portal.usecase.HubServicesAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.hub_services.GetAllTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetAllTransferHandler extends HubServicesAuditableUseCase<GetAllTransfer.Input, GetAllTransfer.Output>
    implements GetAllTransfer {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION);

    private final GetTransfersQuery getTransfersQuery;

    private final ParticipantCache participantCache;

    private final ParticipantUserCache participantUserCache;

    public GetAllTransferHandler(CreateInputAuditCommand createInputAuditCommand,
                                 CreateOutputAuditCommand createOutputAuditCommand,
                                 CreateExceptionAuditCommand createExceptionAuditCommand,
                                 ObjectMapper objectMapper,
                                 PrincipalCache principalCache,
                                 GetTransfersQuery getTransfersQuery,
                                 ParticipantCache participantCache,
                                 ParticipantUserCache participantUserCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

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
            participantData.dfspCode()
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

        return new GetAllTransfer.Output(transferDataList);

    }

}
