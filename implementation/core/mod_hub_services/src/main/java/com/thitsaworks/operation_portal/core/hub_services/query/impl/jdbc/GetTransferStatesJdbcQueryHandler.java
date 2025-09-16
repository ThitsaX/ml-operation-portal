package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferStateData;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.TransferStateDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetTransferStatesQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetTransferStatesJdbcQueryHandler implements GetTransferStatesQuery {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferStatesJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetTransferStatesJdbcQueryHandler(
        @Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws HubServicesException {

        List<TransferStateData> results;
        try {

            results = this.jdbcTemplate.query(
                "SELECT DISTINCT enumeration FROM transferState WHERE enumeration IN ('ABORTED','COMMITTED');",
                new TransferStateDataMapper());

        } catch (Exception e) {
            throw new HubServicesException(HubServicesErrors.HUB_TRANSFER_ERROR.defaultMessage(e.getMessage()));
        }

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        return new Output(results);
    }

}
