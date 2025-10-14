package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementStateData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetSettlementStateQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementStateList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetSettlementStateListHandler
        extends OperationPortalUseCase<GetSettlementStateList.Input, GetSettlementStateList.Output>
        implements GetSettlementStateList {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetSettlementStateListHandler.class);

    private final GetSettlementStateQuery getSettlementStateQuery;

    public GetSettlementStateListHandler(PrincipalCache principalCache,
                                         ActionAuthorizationManager actionAuthorizationManager,
                                         GetSettlementStateQuery getSettlementStateQuery) {

        super(principalCache, actionAuthorizationManager);
        this.getSettlementStateQuery = getSettlementStateQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var output = this.getSettlementStateQuery.execute(new GetSettlementStateQuery.Input());

        List<Output.SettlementStateData> settlementStateStates = new ArrayList<>();

        for (SettlementStateData data : output.settlementStates()) {
            settlementStateStates.add(new Output.SettlementStateData(data.settlementStateId(), data.enumeration()));
        }

        return new Output(settlementStateStates);
    }

}
