package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetCurrentParticipantCurrencies;
import com.thitsaworks.operation_portal.usecase.CentralLedgerUseCase;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetParticipantCurrencies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class GetParticipantCurrenciesHandler
    extends CentralLedgerUseCase<GetParticipantCurrencies.Input, GetParticipantCurrencies.Output>
    implements GetParticipantCurrencies {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantCurrenciesHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final GetCurrentParticipantCurrencies getCurrentParticipantCurrencies;

    public GetParticipantCurrenciesHandler(PrincipalCache principalCache,
                                           GetCurrentParticipantCurrencies getCurrentParticipantCurrencies) {

        super(PERMITTED_ROLES, principalCache);

        this.getCurrentParticipantCurrencies = getCurrentParticipantCurrencies;
    }

    @Override
    protected Output onExecute(Input input) throws OperationPortalException {

        GetCurrentParticipantCurrencies.Output output =
            this.getCurrentParticipantCurrencies.execute(new GetCurrentParticipantCurrencies.Input(input.dfspId()));

        return new Output(output.getCurrencyDataList());
    }

}
