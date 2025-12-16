package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.ParticipantPositionDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantPositionsDataByParticipantNameAndCurrencyQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class GetParticipantPositionsDataByParticipantNameAndCurrencyIdJdbcQueryHandler
    implements GetParticipantPositionsDataByParticipantNameAndCurrencyQuery {

    private static final Logger LOG = LoggerFactory.getLogger(
        GetParticipantPositionsDataByParticipantNameAndCurrencyIdJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetParticipantPositionsDataByParticipantNameAndCurrencyIdJdbcQueryHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws HubServicesException {

        try {

            String query = """
                    SELECT p.name AS dfspId, IFNULL(p.description,p.name) AS dfspName\s
                    ,IFNULL(pc.currencyId,'') AS currency \s
                    ,MIN(CASE WHEN pc.ledgerAccountTypeId = 2 THEN pb.participantCurrencyId END) AS participantSettlementCurrencyId
                    ,MIN(CASE WHEN pc.ledgerAccountTypeId = 1 THEN pp.participantCurrencyId END) AS participantPositionCurrencyId
                    ,MIN(CASE WHEN pc.ledgerAccountTypeId = 1 THEN pc.isActive END) AS isActive
                    FROM participant p\s
                    LEFT JOIN participantCurrency pc ON pc.participantId = p.participantId  \s
                    LEFT JOIN participantPosition pb ON pb.participantCurrencyId = pc.participantCurrencyId AND pc.ledgerAccountTypeId = 2\s
                    LEFT JOIN participantPosition pp ON pp.participantCurrencyId = pc.participantCurrencyId AND pc.ledgerAccountTypeId = 1\s
                    WHERE (? = 'All' OR p.name = ?)\s
                    AND (? = 'All' OR pc.currencyId = ?)\s
                    AND p.name NOT LIKE '%HUB%'\s
                    GROUP BY p.participantId, p.name, p.description, pc.currencyId \s
                    ORDER BY p.name, pc.currencyId;
                    """;
            var result = this.jdbcTemplate.query(query,
                                                 new ParticipantPositionDataMapper(),
                input.getDfspId(),
                input.getDfspId(),
                input.getCurrencyId() != null ? input.getCurrencyId() : "All",
                input.getCurrencyId() != null ? input.getCurrencyId() : "All");

            if (result == null || result.isEmpty()) {

                return null;
            }

            return new Output(result);

        } catch (Exception e) {

            throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_POSITION_ERROR.description(e.getMessage()));
        }

    }

}
