package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.ledger.data.TransferData;
import com.thitsaworks.operation_portal.ledger.query.GetTransfers;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetAllTransfer;
import com.thitsaworks.operation_portal.audit.domain.Auditor;
import com.thitsaworks.operation_portal.audit.exception.UserNotFoundException;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.component.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadTransactional;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.iam.query.data.PrincipalData;
import com.thitsaworks.operation_portal.dfsp_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.cache.ParticipantCache;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.data.ParticipantData;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.data.ParticipantUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllTransferBean extends GetAllTransfer {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferBean.class);

    @Autowired
    private GetTransfers getTransfers;

    @Autowired
    @Qualifier(ParticipantCache.Strategies.DEFAULT)
    private ParticipantCache participantCache;

    @Autowired
    @Qualifier(ParticipantUserCache.Strategies.DEFAULT)
    private ParticipantUserCache participantUserCache;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @CentralLedgerReadTransactional
    public Output onExecute(Input input) throws Exception {

        ParticipantUserData participantUserData = this.participantUserCache.get(input.getParticipantUserId());

        if (participantUserData == null) {

            throw new ParticipantUserNotFoundException(input.getParticipantUserId().getId().toString());
        }

        ParticipantData participantData = this.participantCache.get(participantUserData.getParticipantId());

        if (participantData == null) {

            throw new ParticipantNotFoundException(participantUserData.getParticipantId().getId().toString());
        }

        String fspName = participantData.getDfspCode().getValue();

        GetTransfers.Output output = this.getTransfers.execute(new GetTransfers.Input(input.getFromDate(),
                input.getToDate(),
                input.getTransferId(),
                input.getPayerFspId(),
                input.getPayeeFspId(),
                input.getPayerIdentifierTypeId(),
                input.getPayeeIdentifierTypeId(),
                input.getPayerIdentifierValue(),
                input.getPayeeIdentifierValue(),
                input.getCurrencyId(),
                input.getTransferStateId(),
                fspName,
                input.getTimeZone()));

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

        switch (principalData.getUserRoleType()) {

            case OPERATION:
                return true;
            case SUPERUSER:
            case ADMIN:
            case REPORTING:
                return false;
        }

        return false;
    }

    @Override
    public void onAudit(Input input, Output output) throws UserNotFoundException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        Auditor.audit(this.objectMapper, GetAllTransfer.class, input, null,
                new UserId(Long.valueOf(securityContext.getUserId())));
    }

}
