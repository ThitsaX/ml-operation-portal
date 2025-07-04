package com.thitsaworks.operation_portal.reporting.central_ledger.query.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.CurrencyData;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.mapper.CurrencyDataMapper;
import com.thitsaworks.operation_portal.reporting.central_ledger.exception.CentralLedgerFailureException;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetCurrencies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetCurrenciesHandler implements GetCurrencies {

    private static final Logger LOG = LoggerFactory.getLogger(GetCurrenciesHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetCurrenciesHandler(
        @Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GetCurrencies.Output execute(GetCurrencies.Input input) throws CentralLedgerFailureException {

        List<CurrencyData> results;
        try {

            results = this.jdbcTemplate.query(
                "SELECT currencyId FROM settlementModel WHERE isActive = 1 ORDER BY currencyId;",
                new CurrencyDataMapper());

        } catch (Exception e) {
            throw new CentralLedgerFailureException(e.getMessage());
        }

        if (results.isEmpty()) {

            return new GetCurrencies.Output(new ArrayList<>());
        }

        return new GetCurrencies.Output(results);
    }

}
