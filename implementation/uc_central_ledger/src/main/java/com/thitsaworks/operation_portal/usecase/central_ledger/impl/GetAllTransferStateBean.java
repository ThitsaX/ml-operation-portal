package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.thitsaworks.operation_portal.ledger.data.TransferStateData;
import com.thitsaworks.operation_portal.ledger.query.GetTransferStates;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetAllTransferState;
import com.thitsaworks.operation_portal.component.security.SecurityContext;
import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadTransactional;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.iam.query.data.PrincipalData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetAllTransferStateBean extends GetAllTransferState {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferStateBean.class);

    @Autowired
    private GetTransferStates getTransferStates;

    @Autowired
    @Qualifier(PrincipalCache.Strategies.DEFAULT)
    private PrincipalCache principalCache;

    @Override
    @CentralLedgerReadTransactional
    public GetAllTransferState.Output onExecute(GetAllTransferState.Input input) throws Exception {

        GetTransferStates.Output output = this.getTransferStates.execute(new GetTransferStates.Input());

        List<TransferStateData> transferStateDataList = new ArrayList<>();

        for (TransferStateData data : output.getTransferStateDataList()) {

            transferStateDataList.add(
                    new TransferStateData(
                            data.getTransferStateId(),
                            data.getTransferState()));
        }

        return new GetAllTransferState.Output(transferStateDataList);
    }

    @Override
    protected String getName() {

        return GetAllTransferState.class.getCanonicalName();
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

        return GetAllTransferState.class.getName();
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

}
