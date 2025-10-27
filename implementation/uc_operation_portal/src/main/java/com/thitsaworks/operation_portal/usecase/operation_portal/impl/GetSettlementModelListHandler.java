package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementModelList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

@Service
public class GetSettlementModelListHandler
        extends OperationPortalUseCase<GetSettlementModelList.Input, GetSettlementModelList.Output>
    implements GetSettlementModelList {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetSettlementModelListHandler.class);

    private final SettlementModelQuery settlementModelQuery;

    public GetSettlementModelListHandler(PrincipalCache principalCache,
                                         ActionAuthorizationManager actionAuthorizationManager,
                                         SettlementModelQuery settlementModelQuery) {

        super(principalCache, actionAuthorizationManager);
        this.settlementModelQuery = settlementModelQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        List<SettlementModelData> settlementModelList = this.settlementModelQuery.getSettlementModels();

        var output = new Output(settlementModelList.stream()
                                                   .map(settlementModelData -> new Output.SettlementModelData(
                                                           settlementModelData.settlementModelId(),
                                                           settlementModelData.name(),
                                                           settlementModelData.type(),
                                                           settlementModelData.currencyId(),
                                                           settlementModelData.isActive(),
                                                           settlementModelData.autoCloseWindow(),
                                                           settlementModelData.manualCloseWindow(),
                                                           settlementModelData.zoneId(),
                                                           settlementModelData.requireLiquidityCheck(),
                                                           settlementModelData.autoPositionReset(),
                                                           settlementModelData.adjustPosition(),
                                                           settlementModelData.schedulerConfigIds()
                                                   ))
                                                   .toList());

        return output;
    }

}
