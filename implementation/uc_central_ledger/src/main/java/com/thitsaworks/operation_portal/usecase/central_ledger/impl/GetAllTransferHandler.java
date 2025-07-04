package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.TransferData;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetTransfers;
import com.thitsaworks.operation_portal.usecase.CentralLedgerAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetAllTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetAllTransferHandler extends CentralLedgerAuditableUseCase<GetAllTransfer.Input, GetAllTransfer.Output>
    implements GetAllTransfer {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION);

    private final GetTransfers getTransfers;

    private final ParticipantCache participantCache;

    private final ParticipantUserCache participantUserCache;

    public GetAllTransferHandler(CreateInputAuditCommand createInputAuditCommand,
                                 CreateOutputAuditCommand createOutputAuditCommand,
                                 CreateExceptionAuditCommand createExceptionAuditCommand,
                                 ObjectMapper objectMapper,
                                 PrincipalCache principalCache,
                                 GetTransfers getTransfers,
                                 ParticipantCache participantCache,
                                 ParticipantUserCache participantUserCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.getTransfers = getTransfers;
        this.participantCache = participantCache;
        this.participantUserCache = participantUserCache;

    }

    @Override
    protected Output onExecute(Input input) throws OperationPortalException {

        ParticipantUserData participantUserData = this.participantUserCache.get(input.participantUserId());

        if (participantUserData == null) {

            throw new ParticipantUserNotFoundException(input.participantUserId()
                                                            .getId()
                                                            .toString());
        }

        ParticipantData participantData = this.participantCache.get(participantUserData.participantId());

        if (participantData == null) {

            throw new ParticipantNotFoundException(participantUserData.participantId()
                                                                      .getId()
                                                                      .toString());
        }

        String
            fspName =
            participantData.dfspCode()
                           .getValue();

        GetTransfers.Output output = this.getTransfers.execute(new GetTransfers.Input(input.fromDate(),
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
                    data.getSettlementBatch(),
                    data.getSubmittedOnDate()));
        }

        return new GetAllTransfer.Output(transferDataList);

    }

}
