package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferStateData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetTransferStatesQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetTransferStateList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetTransferStateListHandler
    extends OperationPortalUseCase<GetTransferStateList.Input, GetTransferStateList.Output> implements
                                                                                            GetTransferStateList {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferStateListHandler.class);

    private final GetTransferStatesQuery getTransferStatesQuery;

    public GetTransferStateListHandler(PrincipalCache principalCache,
                                       ActionAuthorizationManager actionAuthorizationManager,
                                       GetTransferStatesQuery getTransferStatesQuery) {

        super(principalCache, actionAuthorizationManager);

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

        return new GetTransferStateList.Output(transferStateDataList);
    }

}
