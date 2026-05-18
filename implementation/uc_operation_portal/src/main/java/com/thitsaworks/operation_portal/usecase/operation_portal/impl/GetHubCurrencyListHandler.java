package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.annotation.ActionMetadata;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.ActionCategory;
import com.thitsaworks.operation_portal.core.hub_services.query.GetCurrenciesQuery;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetHubCurrencyList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@ActionMetadata(category = ActionCategory.PARTICIPANT_PROFILE_AND_FINANCIAL_CONFIGURATION)
public class GetHubCurrencyListHandler
    extends OperationPortalUseCase<GetHubCurrencyList.Input, GetHubCurrencyList.Output>
    implements GetHubCurrencyList {

    private static final Logger LOG = LoggerFactory.getLogger(GetHubCurrencyListHandler.class);

    private final GetCurrenciesQuery getCurrenciesQuery;

    public GetHubCurrencyListHandler(PrincipalCache principalCache,
                                     ActionAuthorizationManager actionAuthorizationManager,
                                     GetCurrenciesQuery getCurrenciesQuery) {

        super(principalCache, actionAuthorizationManager);

        this.getCurrenciesQuery = getCurrenciesQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetCurrenciesQuery.Output output = this.getCurrenciesQuery.execute(
            new GetCurrenciesQuery.Input());

        return new GetHubCurrencyList.Output(output.getCurrencyDataList());
    }

}
