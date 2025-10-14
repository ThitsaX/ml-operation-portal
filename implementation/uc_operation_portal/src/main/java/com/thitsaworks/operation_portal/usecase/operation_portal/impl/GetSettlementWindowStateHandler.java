package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowStateData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetSettlementWindowStateQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementWindowState;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetSettlementWindowStateHandler extends OperationPortalUseCase<GetSettlementWindowState.Input, GetSettlementWindowState.Output>
        implements GetSettlementWindowState {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetSettlementWindowStateHandler.class);

    private final GetSettlementWindowStateQuery settlementWindowStateQuery;

    public GetSettlementWindowStateHandler(PrincipalCache principalCache,
                                           ActionAuthorizationManager actionAuthorizationManager,
                                           GetSettlementWindowStateQuery settlementWindowStateQuery) {

        super(principalCache, actionAuthorizationManager);
        this.settlementWindowStateQuery = settlementWindowStateQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var output = this.settlementWindowStateQuery.execute(new GetSettlementWindowStateQuery.Input());

        List<SettlementWindowStateData> settlementWindowStates= new ArrayList<>();

        for (SettlementWindowStateData data : output.settlementWindowStates()){
             settlementWindowStates.add(new SettlementWindowStateData(data.settlementWindowStateId(), data.enumeration()));
        }
        return  new Output(settlementWindowStates);
    }

}
