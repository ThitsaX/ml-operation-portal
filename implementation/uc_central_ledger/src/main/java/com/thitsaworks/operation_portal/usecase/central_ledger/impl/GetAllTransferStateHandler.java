package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.TransferStateData;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetTransferStates;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetAllTransferState;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllTransferStateHandler extends GetAllTransferState {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferStateHandler.class);

    private final GetTransferStates getTransferStates;

    private final PrincipalCache principalCache;

    @Override
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
                this.principalCache.get(new AccessKey(securityContext.accessKey()));

        return switch (principalData.userRoleType()) {
            case OPERATION -> true;
            case SUPERUSER, ADMIN, REPORTING -> false;
        };

    }

}
