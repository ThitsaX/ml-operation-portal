package com.thitsaworks.operation_portal.usecase.central_ledger.impl;

import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetCurrencies;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetCurrentParticipantCurrencies;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetParticipantCurrencies;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetParticipantCurrenciesHandler extends GetParticipantCurrencies {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantCurrenciesHandler.class);

    private final GetCurrentParticipantCurrencies getCurrentParticipantCurrencies;

    @Override
    public Output onExecute(Input input) throws Exception {

        GetCurrentParticipantCurrencies.Output output =
                this.getCurrentParticipantCurrencies.execute(new GetCurrentParticipantCurrencies.Input(input.dfspId()));

        return new Output(output.getCurrencyDataList());
    }

    @Override
    protected String getName() {

        return GetCurrencies.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_central_ledger";
    }

    @Override
    protected String getId() {

        return GetCurrencies.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        return true;
    }
}
