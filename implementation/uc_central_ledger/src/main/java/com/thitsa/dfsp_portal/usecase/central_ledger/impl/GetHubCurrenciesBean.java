package com.thitsa.dfsp_portal.usecase.central_ledger.impl;

import com.thitsa.dfsp_portal.ledger.query.GetCurrencies;
import com.thitsa.dfsp_portal.usecase.central_ledger.GetHubCurrencies;
import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetHubCurrenciesBean extends GetHubCurrencies {

    private static final Logger LOG = LoggerFactory.getLogger(GetHubCurrenciesBean.class);

    @Autowired
    private GetCurrencies getCurrencies;

    @Override
    @CentralLedgerReadTransactional
    public Output onExecute(Input input) throws Exception {

        GetCurrencies.Output output = this.getCurrencies.execute(new GetCurrencies.Input());

        return new GetHubCurrencies.Output(output.getCurrencyDataList());
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
