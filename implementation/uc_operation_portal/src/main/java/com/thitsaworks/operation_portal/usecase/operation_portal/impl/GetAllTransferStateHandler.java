package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferStateData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetTransferStatesQuery;

import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAllTransferState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetAllTransferStateHandler
    extends OperationPortalUseCase<GetAllTransferState.Input, GetAllTransferState.Output> implements GetAllTransferState {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllTransferStateHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION);

    private final GetTransferStatesQuery getTransferStatesQuery;

    public GetAllTransferStateHandler(PrincipalCache principalCache,
                                      GetTransferStatesQuery getTransferStatesQuery) {

        super(PERMITTED_ROLES, principalCache);

        this.getTransferStatesQuery = getTransferStatesQuery;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetTransferStatesQuery.Output output = this.getTransferStatesQuery.execute(new GetTransferStatesQuery.Input());

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
