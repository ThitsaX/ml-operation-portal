package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.FinancialDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.query.GetFinancialDataQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class GetFinancialDataJdbcQueryHandler implements GetFinancialDataQuery {

    private static final Logger LOG = LoggerFactory.getLogger(GetFinancialDataJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetFinancialDataJdbcQueryHandler(@Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) {

        var result = this.jdbcTemplate.query(
                "SELECT p.description AS dfspName, IFNULL(pc.currencyId,'') AS currency, IFNULL(ROUND( SUM(IFNULL(pb.value,0)),2),0) AS balance" +
                        ", IFNULL(ROUND(SUM(IFNULL(pp.value,0)),2),0) AS currentPosition, IFNULL(ROUND( SUM(IFNULL(pl.value,0)),2),0) AS ndc,\n" +
                        " IFNULL(ROUND(((SUM(IFNULL(pp.value,0))/SUM(IFNULL(pl.value,0))) * 100),2),0) AS ndcUsed \n" +
                        "FROM participant p\n" +
                        "LEFT JOIN participantCurrency pc ON pc.participantId = p.participantId\n" +
                        "LEFT JOIN participantLimit pl ON pc.participantCurrencyId = pl.participantCurrencyId AND pl.isActive = 1\n" +
                        "LEFT JOIN participantPosition pb ON pb.participantCurrencyId = pc.participantCurrencyId AND pc.ledgerAccountTypeId = 2\n" +
                        "LEFT JOIN participantPosition pp ON pp.participantCurrencyId = pc.participantCurrencyId AND pc.ledgerAccountTypeId = 1\n" +
                        "WHERE NAME =? AND pc.isActive = 1 GROUP BY pc.currencyId;", new FinancialDataMapper(),
                input.getFspID());

        if (result == null || result.isEmpty()) {

            return null;
        }

        return new Output(result);
    }

}
