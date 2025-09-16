package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantData;
import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantDetailData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.HubParticipantQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
                "SELECT participantId, name, description, isActive, createdDate, createdBy, isProxy FROM participant",
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
                "SELECT participantId, name, description, isActive, createdDate, createdBy, isProxy FROM participant WHERE name = ?",
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
            throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_ERROR.defaultMessage(
                    "Participant with name [" + name + "] cannot find on Hub"));
        }

        return participantDataList.getFirst();

    }

    @Override
    public List<HubParticipantDetailData> getHubParticipantDetailDataList()
            throws HubServicesException {

        final String sql =
                "SELECT p.participantId, p.name AS participantName, " +
                        "       pc.participantCurrencyId, pc.currencyId, pc.ledgerAccountTypeId, " +
                        "       lat.name AS ledgerAccountTypeName " +
                        "FROM participant p " +
                        "LEFT JOIN participantCurrency pc ON pc.participantId = p.participantId " +
                        "LEFT JOIN ledgerAccountType lat ON lat.ledgerAccountTypeId = pc.ledgerAccountTypeId " +
                        "ORDER BY p.participantId, pc.currencyId, pc.ledgerAccountTypeId";

        Map<Integer, HubParticipantDetailData> map = new LinkedHashMap<>();
        this.jdbcTemplate.query(sql, rs -> {
            Integer participantId = rs.getInt("participantId");
            String participantName = rs.getString("participantName");
            HubParticipantDetailData participantDetailData = map.computeIfAbsent(participantId,
                                                                                 k -> new HubParticipantDetailData(
                                                                                         participantId,
                                                                                         participantName,
                                                                                         new ArrayList<>()));

            participantDetailData.getAccounts().add(new HubParticipantDetailData.AccountData(rs.getInt(
                    "participantCurrencyId"),
                                                                                             rs.getString("currencyId"),
                                                                                             rs.getInt(
                                                                                                     "ledgerAccountTypeId"),
                                                                                             rs.getString(
                                                                                                     "ledgerAccountTypeName")));

        });

        return new ArrayList<>(map.values());

    }

}
