package com.thitsaworks.operation_portal.reporting.central_ledger.query.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.TransferStateData;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.mapper.TransferStateDataMapper;
import com.thitsaworks.operation_portal.reporting.central_ledger.exception.CentralLedgerFailureException;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetTransferStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetTransferStatesHandler implements GetTransferStates {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferStatesHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetTransferStatesHandler(
        @Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws CentralLedgerFailureException {

        List<TransferStateData> results;
        try {

            results = this.jdbcTemplate.query(
                "SELECT DISTINCT enumeration FROM transferState WHERE enumeration IN ('ABORTED','COMMITTED');",
                new TransferStateDataMapper());

        } catch (Exception e) {

            throw new CentralLedgerFailureException(e.getMessage());
        }

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        return new Output(results);
    }

}
