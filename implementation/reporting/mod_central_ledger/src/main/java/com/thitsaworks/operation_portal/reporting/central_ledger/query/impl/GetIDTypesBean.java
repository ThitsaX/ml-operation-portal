package com.thitsaworks.operation_portal.reporting.central_ledger.query.impl;

import com.thitsaworks.operation_portal.reporting.central_ledger.data.mapper.IDTypeDataMapper;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetIDTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GetIDTypesBean implements GetIDTypes {

    private static final Logger LOG = LoggerFactory.getLogger(GetIDTypesBean.class);

    @Autowired
    private JdbcTemplate centralLedgerJdbcTemplate;

    @Override
    public Output execute(Input input) throws Exception {

        var results = centralLedgerJdbcTemplate.query(
                "SELECT name as partyIdentifierTypeId, name FROM partyIdentifierType ORDER BY partyIdentifierTypeId;",
                new IDTypeDataMapper());

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        return new Output(results);
    }

}
