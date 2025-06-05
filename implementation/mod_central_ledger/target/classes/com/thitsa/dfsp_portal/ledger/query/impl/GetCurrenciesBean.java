package com.thitsa.dfsp_portal.ledger.query.impl;

import com.thitsa.dfsp_portal.ledger.data.mapper.CurrencyDataMapper;
import com.thitsa.dfsp_portal.ledger.query.GetCurrencies;
import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@CentralLedgerReadTransactional
public class GetCurrenciesBean implements GetCurrencies {

    private static final Logger LOG = LoggerFactory.getLogger(GetCurrenciesBean.class);

    @Autowired
    private JdbcTemplate centralLedgerJdbcTemplate;

    @Override
    public GetCurrencies.Output execute(GetCurrencies.Input input) throws Exception {

        var results = centralLedgerJdbcTemplate.query(
                "SELECT currencyId FROM settlementModel WHERE isActive = 1 ORDER BY currencyId;",
                new CurrencyDataMapper());

        if (results == null || results.isEmpty()) {

            return new GetCurrencies.Output(new ArrayList<>());
        }

        return new GetCurrencies.Output(results);
    }
}
