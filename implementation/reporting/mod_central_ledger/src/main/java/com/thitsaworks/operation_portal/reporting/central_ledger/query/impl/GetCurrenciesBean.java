package com.thitsaworks.operation_portal.reporting.central_ledger.query.impl;

import com.thitsaworks.operation_portal.reporting.central_ledger.data.mapper.CurrencyDataMapper;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetCurrencies;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GetCurrenciesBean implements GetCurrencies {

    private static final Logger LOG = LoggerFactory.getLogger(GetCurrenciesBean.class);

    private final JdbcTemplate jdbcTemplate;

    public GetCurrenciesBean(@Qualifier(PersistenceQualifiers.Reporting.Read_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        assert jdbcTemplate != null;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GetCurrencies.Output execute(GetCurrencies.Input input) throws Exception {

        var results = jdbcTemplate.query(
                "SELECT currencyId FROM settlementModel WHERE isActive = 1 ORDER BY currencyId;",
                new CurrencyDataMapper());

        if (results.isEmpty()) {

            return new GetCurrencies.Output(new ArrayList<>());
        }

        return new GetCurrencies.Output(results);
    }

}
