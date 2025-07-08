package com.thitsaworks.operation_portal.reporting.central_ledger.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.CurrencyData;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.mapper.CurrencyDataMapper;
import com.thitsaworks.operation_portal.reporting.central_ledger.exception.CentralLedgerErrors;
import com.thitsaworks.operation_portal.reporting.central_ledger.exception.CentralLedgerException;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetCurrentParticipantCurrenciesQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetCurrentParticipantCurrenciesJdbcQueryHandler implements GetCurrentParticipantCurrenciesQuery {

    private static final Logger LOG = LoggerFactory.getLogger(GetCurrentParticipantCurrenciesJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetCurrentParticipantCurrenciesJdbcQueryHandler(
        @Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws CentralLedgerException {

        List<CurrencyData> results;
        try {

            results = this.jdbcTemplate.query(
                "SELECT currencyId FROM participantCurrency\n" +
                    "INNER JOIN participant\n" +
                    " ON participant.participantId =participantCurrency.participantId\n" +
                    " WHERE participantCurrency.isActive =1 and name=? GROUP BY currencyId ORDER BY currencyId",
                new CurrencyDataMapper(), input.getDfspId());

        } catch (Exception e) {
            throw new CentralLedgerException(CentralLedgerErrors.CENTRAL_LEDGER_FAILURE_EXCEPTION);
        }

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        return new Output(results);
    }

}
