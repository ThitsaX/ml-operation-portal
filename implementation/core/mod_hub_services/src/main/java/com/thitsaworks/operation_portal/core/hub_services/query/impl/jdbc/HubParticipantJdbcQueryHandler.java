package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantAccountData;
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
    public List<HubParticipantData> getParticipantList() {

        return this.jdbcTemplate.query(
                "SELECT participantId, participantName, description, isActive, createdDate, createdBy, isProxy FROM participant",
                (rs, rowNum) -> new HubParticipantData(
                        rs.getString("participantId"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBoolean("isActive"),
                        rs.getTimestamp("createdDate").toInstant(),
                        rs.getString("createdBy")
                ));

    }

    @Override
    public HubParticipantData getByName(String name) throws HubServicesException {

        List<HubParticipantData> participantDataList = this.jdbcTemplate.query(
                "SELECT participantId, participantName, description, isActive, createdDate, createdBy, isProxy FROM participant WHERE participantName = ?",
                new Object[]{name},
                (rs, rowNum) -> new HubParticipantData(
                        rs.getString("participantId"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBoolean("isActive"),
                        rs.getTimestamp("createdDate").toInstant(),
                        rs.getString("createdBy")
                ));

        if (participantDataList.isEmpty()) {
            throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_NOT_FOUND);
        }

        return participantDataList.getFirst();

    }

    @Override
    public HubParticipantAccountData getAccountData(Integer participantId, Integer participantCurrencyId)
            throws HubServicesException {

        List<HubParticipantAccountData> hubParticipantAccountDataList = this.jdbcTemplate.query(
                """
                            SELECT 
                                p.participantName  AS participantName,
                                pc.ledgerAccountTypeId,
                                pc.currencyId,
                                lat.participantName AS ledgerAccountTypeName
                            FROM participantCurrency pc
                            JOIN ledgerAccountType lat 
                                ON pq.ledgerAccountTypeId = lat.ledgerAccountTypeId
                            JOIN participant p 
                                ON pq.participantId = p.participantId
                            WHERE pq.participantId = ? 
                              AND pq.participantCurrencyId = ? 
                        """,
                new Object[]{participantId, participantCurrencyId},
                (rs, rowNum) -> new HubParticipantAccountData(
                        participantId,
                        rs.getString("participantName"),
                        participantCurrencyId,
                        rs.getString("currencyId"),
                        rs.getInt("ledgerAccountTypeId"),
                        rs.getString("ledgerAccountTypeName")));

        if (hubParticipantAccountDataList.isEmpty()) {
            throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_NOT_FOUND);
        }

        return hubParticipantAccountDataList.getFirst();

    }

}
