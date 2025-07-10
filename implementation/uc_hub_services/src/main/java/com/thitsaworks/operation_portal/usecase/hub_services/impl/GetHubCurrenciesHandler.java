package com.thitsaworks.operation_portal.usecase.hub_services.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.hub_services.query.GetCurrenciesQuery;
import com.thitsaworks.operation_portal.usecase.HubServicesUseCase;
import com.thitsaworks.operation_portal.usecase.hub_services.GetHubCurrencies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class GetHubCurrenciesHandler extends HubServicesUseCase<GetHubCurrencies.Input, GetHubCurrencies.Output>
    implements GetHubCurrencies {

    private static final Logger LOG = LoggerFactory.getLogger(GetHubCurrenciesHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final GetCurrenciesQuery getCurrenciesQuery;

    public GetHubCurrenciesHandler(PrincipalCache principalCache,
                                   GetCurrenciesQuery getCurrenciesQuery) {

        super(PERMITTED_ROLES, principalCache);

        this.getCurrenciesQuery = getCurrenciesQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetCurrenciesQuery.Output output = this.getCurrenciesQuery.execute(new GetCurrenciesQuery.Input());

        return new GetHubCurrencies.Output(output.getCurrencyDataList());
    }

}
