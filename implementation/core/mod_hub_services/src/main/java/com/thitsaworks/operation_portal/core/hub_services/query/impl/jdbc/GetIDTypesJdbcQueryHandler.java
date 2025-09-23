package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;

import com.thitsaworks.operation_portal.core.hub_services.data.IDTypeData;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.IDTypeDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetIDTypesQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetIDTypesJdbcQueryHandler implements GetIDTypesQuery {

    private static final Logger LOG = LoggerFactory.getLogger(GetIDTypesJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetIDTypesJdbcQueryHandler(@Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws HubServicesException {

        List<IDTypeData> results;
        try {

            results = this.jdbcTemplate.query(
                    "SELECT name as partyIdentifierTypeId, name FROM partyIdentifierType ORDER BY partyIdentifierTypeId;",
                new IDTypeDataMapper());

        } catch (Exception e) {
            throw new HubServicesException(HubServicesErrors.PARTY_IDENTIFIER_TYPE_ID.description(e.getMessage()));
        }

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        return new Output(results);
    }

}
