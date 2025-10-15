package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.FinancialDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantPositionsDataQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class GetParticipantPositionsDataJdbcQueryHandler implements GetParticipantPositionsDataQuery {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantPositionsDataJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetParticipantPositionsDataJdbcQueryHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws HubServicesException {

        try {
            var result = this.jdbcTemplate.query(
                "SELECT p.name AS dfspId, IFNULL(p.description,p.name) AS dfspName \n" +
                    ",IFNULL(pc.currencyId,'') AS currency \n" +
                    ",IFNULL(ROUND( SUM(IFNULL(pb.value,0)),2),0) AS balance \n" +
                    ",IFNULL(ROUND(SUM(IFNULL(pp.value,0)),2),0) AS currentPosition, 0 AS ndcPercent \n" +
                    ",IFNULL(ROUND(SUM(IFNULL(pl.value, 0)), 2), 0) AS ndc \n" +
                    ",IFNULL(ROUND(((SUM(IFNULL(pp.value,0))/SUM(IFNULL(pl.value,0))) * 100),2),0) AS ndcUsed\n" +
                    ",MIN(CASE WHEN pc.ledgerAccountTypeId = 2 THEN pb.participantCurrencyId END) AS participantSettlementCurrencyId\n" +
                    ",MIN(CASE WHEN pc.ledgerAccountTypeId = 1 THEN pp.participantCurrencyId END) AS participantPositionCurrencyId\n" +
                    ",MIN(CASE WHEN pc.ledgerAccountTypeId = 1 THEN pc.isActive END) AS isActive\n" +
                    "FROM participant p \n" +
                    "LEFT JOIN participantCurrency pc ON pc.participantId = p.participantId \n" +
                    "LEFT JOIN participantLimit pl ON pc.participantCurrencyId = pl.participantCurrencyId AND pl.isActive = 1 \n" +
                    "LEFT JOIN participantPosition pb ON pb.participantCurrencyId = pc.participantCurrencyId AND pc.ledgerAccountTypeId = 2 \n" +
                    "LEFT JOIN participantPosition pp ON pp.participantCurrencyId = pc.participantCurrencyId AND pc.ledgerAccountTypeId = 1 \n" +
                    "WHERE (? = 'All' OR p.name = ?) AND p.name NOT LIKE '%HUB%' GROUP BY p.participantId, p.name, p.description, pc.currencyId  ORDER BY p.name, pc.currencyId;",
                new FinancialDataMapper(),
                input.getFspID(),
                input.getFspID());

            if (result == null || result.isEmpty()) {

                return null;
            }

            return new Output(result);

        } catch (Exception e) {

            throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_POSITION_ERROR.description(e.getMessage()));
        }

    }

}
