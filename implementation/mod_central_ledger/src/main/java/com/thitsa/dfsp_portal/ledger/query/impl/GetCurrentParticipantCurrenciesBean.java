package com.thitsa.dfsp_portal.ledger.query.impl;

import com.thitsa.dfsp_portal.ledger.data.mapper.CurrencyDataMapper;
import com.thitsa.dfsp_portal.ledger.query.GetCurrentParticipantCurrencies;
import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@CentralLedgerReadTransactional
public class GetCurrentParticipantCurrenciesBean implements GetCurrentParticipantCurrencies {

    private static final Logger LOG = LoggerFactory.getLogger(GetCurrentParticipantCurrenciesBean.class);

    @Autowired
    private JdbcTemplate centralLedgerJdbcTemplate;

    @Override
    public Output execute(Input input) throws Exception {

        var results = centralLedgerJdbcTemplate.query(
                "SELECT currencyId FROM participantCurrency\n" +
                        "INNER JOIN participant\n" +
                        " ON participant.participantId =participantCurrency.participantId\n" +
                        " WHERE participantCurrency.isActive =1 and name=? GROUP BY currencyId ORDER BY currencyId",
                new CurrencyDataMapper(), input.getDfspId());

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        return new Output(results);
    }
}
