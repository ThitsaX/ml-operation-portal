package com.thitsaworks.operation_portal.reporting.central_ledger.query.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.IDTypeData;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.mapper.IDTypeDataMapper;
import com.thitsaworks.operation_portal.reporting.central_ledger.exception.CentralLedgerErrors;
import com.thitsaworks.operation_portal.reporting.central_ledger.exception.CentralLedgerException;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetIDTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetIDTypesHandler implements GetIDTypes {

    private static final Logger LOG = LoggerFactory.getLogger(GetIDTypesHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetIDTypesHandler(@Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws CentralLedgerException {

        List<IDTypeData> results;
        try {

            results = this.jdbcTemplate.query(
                "SELECT name as partyIdentifierTypeId, name FROM partyIdentifierType ORDER BY partyIdentifierTypeId;",
                new IDTypeDataMapper());

        } catch (Exception e) {
            throw new CentralLedgerException(CentralLedgerErrors.CENTRAL_LEDGER_FAILURE_EXCEPTION);
        }

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        return new Output(results);
    }

}
