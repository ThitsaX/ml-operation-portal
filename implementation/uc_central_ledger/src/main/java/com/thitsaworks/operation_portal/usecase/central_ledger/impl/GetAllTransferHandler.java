package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.core.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.core.audit.model.Auditor;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.TransferData;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetTransfers;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetAllTransfer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllTransferHandler extends GetAllTransfer {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferHandler.class);

    private final GetTransfers getTransfers;

    private final ParticipantCache participantCache;

    private final ParticipantUserCache participantUserCache;

    private final PrincipalCache principalCache;

    private final ObjectMapper objectMapper;

    @Override
    public Output onExecute(Input input) throws Exception {

        ParticipantUserData participantUserData = this.participantUserCache.get(input.participantUserId());

        if (participantUserData == null) {

            throw new ParticipantUserNotFoundException(input.participantUserId().getId().toString());
        }

        ParticipantData participantData = this.participantCache.get(participantUserData.participantId());

        if (participantData == null) {

            throw new ParticipantNotFoundException(participantUserData.participantId().getId().toString());
        }

        String fspName = participantData.dfspCode().getValue();

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

    @Override
    protected String getName() {

        return GetAllTransfer.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_central_ledger";
    }

    @Override
    protected String getId() {

        return GetAllTransfer.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        SecurityContext securityContext = (SecurityContext) userDetails;

        PrincipalData principalData =
                this.principalCache.get(new AccessKey(Long.parseLong(securityContext.getAccessKey())));

        return switch (principalData.userRoleType()) {
            case OPERATION -> true;
            case SUPERUSER, ADMIN, REPORTING -> false;
        };

    }

    @Override
    public void onAudit(Input input, Output output) throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GetAllTransfer.class, input, null,
                      new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
