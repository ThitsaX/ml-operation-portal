package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;

import com.thitsaworks.operation_portal.core.hub_services.data.CurrencyData;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.CurrencyDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetCurrenciesQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetCurrenciesJdbcQueryHandler implements GetCurrenciesQuery {

    private static final Logger LOG = LoggerFactory.getLogger(GetCurrenciesJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetCurrenciesJdbcQueryHandler(
        @Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GetCurrenciesQuery.Output execute(GetCurrenciesQuery.Input input) throws HubServicesException {

        List<CurrencyData> results;
        try {

            results = this.jdbcTemplate.query(
                "SELECT currencyId FROM settlementModel WHERE isActive = 1 ORDER BY currencyId;",
                new CurrencyDataMapper());

        } catch (Exception e) {
            throw new HubServicesException(HubServicesErrors.CENTRAL_LEDGER_FAILURE_EXCEPTION);
        }

        if (results.isEmpty()) {

            return new GetCurrenciesQuery.Output(new ArrayList<>());
        }

        return new GetCurrenciesQuery.Output(results);
    }

}
