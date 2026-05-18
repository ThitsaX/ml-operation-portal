package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.annotation.ActionMetadata;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.ActionCategory;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementSchedulerQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementSchedulerList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@ActionMetadata(category = ActionCategory.SCHEDULER_AND_JOB_CONFIGURATION)
public class GetSettlementSchedulerListHandler
    extends OperationPortalUseCase<GetSettlementSchedulerList.Input, GetSettlementSchedulerList.Output>
    implements GetSettlementSchedulerList {

    private static final Logger LOG = LoggerFactory.getLogger(
        GetSettlementSchedulerListHandler.class);

    private final SettlementModelQuery settlementModelQuery;

    private final SettlementSchedulerQuery settlementSchedulerQuery;

    public GetSettlementSchedulerListHandler(PrincipalCache principalCache,
                                             SettlementModelQuery settlementModelQuery,
                                             SettlementSchedulerQuery settlementSchedulerQuery,
                                             ActionAuthorizationManager actionAuthorizationManager) {

        super(principalCache, actionAuthorizationManager);

        this.settlementModelQuery = settlementModelQuery;
        this.settlementSchedulerQuery = settlementSchedulerQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        SettlementModelData settlementModelData = this.settlementModelQuery.get(
            input.settlementModelId());

        return new Output(this.settlementSchedulerQuery.getSettlementSchedulers(
            settlementModelData.settlementModelId()));
    }

}
