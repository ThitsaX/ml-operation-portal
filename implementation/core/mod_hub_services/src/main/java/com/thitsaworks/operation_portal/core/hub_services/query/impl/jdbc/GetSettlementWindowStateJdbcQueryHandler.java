package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowStateData;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.SettlementWindowStateDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.query.GetSettlementWindowStateQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetSettlementWindowStateJdbcQueryHandler implements GetSettlementWindowStateQuery {

    private static final Logger logger = LoggerFactory.getLogger(GetSettlementWindowStateJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    public GetSettlementWindowStateJdbcQueryHandler(
            @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) {
        List<SettlementWindowStateData> results;
        try {
            results = this.jdbcTemplate.query(
                    "SELECT DISTINCT settlementWindowStateId, enumeration FROM settlementWindowState WHERE enumeration IN ('ABORTED','CLOSED','FAILED','OPEN','PENDING_SETTLEMENT','PROCESSING','SETTLED');",
                    new SettlementWindowStateDataMapper());
        } catch (Exception e) {
            // Handle exception
            throw new RuntimeException("Error fetching settlement window states", e);
        }
        if (results == null || results.isEmpty()) {
            return new Output(new ArrayList<>());
        }

        return new Output(results);

    }

}
