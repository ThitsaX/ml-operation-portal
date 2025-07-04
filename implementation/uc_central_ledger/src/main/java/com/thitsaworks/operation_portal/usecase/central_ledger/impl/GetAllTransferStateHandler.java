package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.TransferStateData;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetTransferStates;
import com.thitsaworks.operation_portal.usecase.CentralLedgerUseCase;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetAllTransferState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetAllTransferStateHandler
    extends CentralLedgerUseCase<GetAllTransferState.Input, GetAllTransferState.Output> implements GetAllTransferState {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferStateHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION);

    private final GetTransferStates getTransferStates;

    public GetAllTransferStateHandler(PrincipalCache principalCache,
                                      GetTransferStates getTransferStates) {

        super(PERMITTED_ROLES, principalCache);

        this.getTransferStates = getTransferStates;

    }

    @Override
    protected Output onExecute(Input input) throws OperationPortalException {

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

}
