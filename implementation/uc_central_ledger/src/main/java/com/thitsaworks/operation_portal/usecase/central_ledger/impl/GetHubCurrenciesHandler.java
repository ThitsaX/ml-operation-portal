package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.central_ledger.exception.CentralLedgerException;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetCurrencies;
import com.thitsaworks.operation_portal.usecase.CentralLedgerUseCase;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetHubCurrencies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class GetHubCurrenciesHandler extends CentralLedgerUseCase<GetHubCurrencies.Input, GetHubCurrencies.Output>
    implements GetHubCurrencies {

    private static final Logger LOG = LoggerFactory.getLogger(GetHubCurrenciesHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final GetCurrencies getCurrencies;

    public GetHubCurrenciesHandler(PrincipalCache principalCache,
                                   GetCurrencies getCurrencies) {

        super(PERMITTED_ROLES, principalCache);

        this.getCurrencies = getCurrencies;
    }

    @Override
    protected Output onExecute(Input input) throws CentralLedgerException {

        GetCurrencies.Output output = this.getCurrencies.execute(new GetCurrencies.Input());

        return new GetHubCurrencies.Output(output.getCurrencyDataList());
    }

}
