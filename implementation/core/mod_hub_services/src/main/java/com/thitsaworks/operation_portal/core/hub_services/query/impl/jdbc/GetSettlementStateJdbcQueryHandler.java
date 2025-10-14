package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementStateData;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.SettlementStateDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.query.GetSettlementStateQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetSettlementStateJdbcQueryHandler implements GetSettlementStateQuery {

    private static final Logger logger = LoggerFactory.getLogger(GetSettlementStateJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetSettlementStateJdbcQueryHandler(
            @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) {

        List<SettlementStateData> results;
        try {
            results = this.jdbcTemplate.query(
                    "SELECT DISTINCT settlementStateId, enumeration FROM settlementState WHERE enumeration IN ('ABORTED','PENDING_SETTLEMENT','PS_TRANSFERS_COMMITTED','PS_TRANSFERS_RECORDED','PS_TRANSFERS_RESERVED','SETTLED','SETTLING');",
                    new SettlementStateDataMapper());
        } catch (Exception e) {
            // Handle exception
            throw new RuntimeException("Error fetching settlement states", e);
        }
        if (results == null || results.isEmpty()) {
            return new Output(new ArrayList<>());
        }
        return new Output(results);

    }

}