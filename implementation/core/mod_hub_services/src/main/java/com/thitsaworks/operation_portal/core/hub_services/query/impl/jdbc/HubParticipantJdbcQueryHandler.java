package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.HubParticipantQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HubParticipantJdbcQueryHandler implements HubParticipantQuery {

    private static final Logger LOG = LoggerFactory.getLogger(HubParticipantJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HubParticipantJdbcQueryHandler(
            @Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<HubParticipantData> getParticipants() {

        return this.jdbcTemplate.query(
                "SELECT participantId, name, description, isActive, createdDate, createdBy, isProxy FROM participant",
                (rs, rowNum) -> new HubParticipantData(
                        rs.getString("participantId"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBoolean("isActive"),
                        rs.getTimestamp("createdDate").toInstant(),
                        rs.getString("createdBy"),
                        rs.getBoolean("isProxy")
                ));

    }

    @Override
    public HubParticipantData getByName(String name) throws HubServicesException {

        List<HubParticipantData> participantDataList = this.jdbcTemplate.query(
                "SELECT participantId, name, description, isActive, createdDate, createdBy, isProxy FROM participant WHERE name = ?",
                new Object[]{name},
                (rs, rowNum) -> new HubParticipantData(
                        rs.getString("participantId"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBoolean("isActive"),
                        rs.getTimestamp("createdDate").toInstant(),
                        rs.getString("createdBy"),
                        rs.getBoolean("isProxy")
                ));

        if (participantDataList.isEmpty()) {
            throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_NOT_FOUND);
        }

        return participantDataList.getFirst();

    }

}
