package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.hub_services.query.GetCurrentParticipantCurrenciesQuery;

import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantCurrencies;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class GetParticipantCurrenciesHandler
    extends OperationPortalUseCase<GetParticipantCurrencies.Input, GetParticipantCurrencies.Output>
    implements GetParticipantCurrencies {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantCurrenciesHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final GetCurrentParticipantCurrenciesQuery getCurrentParticipantCurrenciesQuery;

    public GetParticipantCurrenciesHandler(PrincipalCache principalCache,
                                           ActionAuthorizationManager actionAuthorizationManager,
                                           GetCurrentParticipantCurrenciesQuery getCurrentParticipantCurrenciesQuery) {

        super(PERMITTED_ROLES,
              principalCache,
              actionAuthorizationManager);

        this.getCurrentParticipantCurrenciesQuery = getCurrentParticipantCurrenciesQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        GetCurrentParticipantCurrenciesQuery.Output output =
            this.getCurrentParticipantCurrenciesQuery.execute(new GetCurrentParticipantCurrenciesQuery.Input(input.dfspId()));

        return new Output(output.getCurrencyDataList());
    }

}
