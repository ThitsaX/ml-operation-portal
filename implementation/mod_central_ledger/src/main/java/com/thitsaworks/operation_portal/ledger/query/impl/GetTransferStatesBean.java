package com.thitsaworks.operation_portal.ledger.query.impl;

import com.thitsaworks.operation_portal.ledger.data.mapper.TransferStateDataMapper;
import com.thitsaworks.operation_portal.ledger.query.GetTransferStates;
import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@CentralLedgerReadTransactional
public class GetTransferStatesBean implements GetTransferStates {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferStatesBean.class);

    @Autowired
    private JdbcTemplate centralLedgerJdbcTemplate;

    @Override
    public Output execute(Input input) throws Exception {

        var results = centralLedgerJdbcTemplate.query(
                "SELECT DISTINCT enumeration FROM transferState WHERE enumeration IN ('ABORTED','COMMITTED');", new TransferStateDataMapper());

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        return new Output(results);
    }

}
