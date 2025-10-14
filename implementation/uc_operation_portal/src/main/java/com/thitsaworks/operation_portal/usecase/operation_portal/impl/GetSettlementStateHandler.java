package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementStateData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetSettlementStateQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementState;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetSettlementStateHandler
        extends OperationPortalUseCase<GetSettlementState.Input, GetSettlementState.Output>
        implements GetSettlementState {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetSettlementStateHandler.class);

    private final GetSettlementStateQuery getSettlementStateQuery;

    public GetSettlementStateHandler(PrincipalCache principalCache,
                                     ActionAuthorizationManager actionAuthorizationManager,
                                     GetSettlementStateQuery getSettlementStateQuery) {

        super(principalCache, actionAuthorizationManager);
        this.getSettlementStateQuery = getSettlementStateQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var output = this.getSettlementStateQuery.execute(new GetSettlementStateQuery.Input());

        List<SettlementStateData> settlementStateStates = new ArrayList<>();

        for (SettlementStateData data : output.settlementStates()) {
            settlementStateStates.add(new SettlementStateData(data.settlementStateId(), data.enumeration()));
        }
        return new Output(settlementStateStates);
    }

}
