package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.ParticipantBalanceData;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.ParticipantBalanceDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantBalanceByCurrencyIdQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetParticipantBalanceByCurrencyIdJdbcQueryHandler implements GetParticipantBalanceByCurrencyIdQuery {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantBalanceByCurrencyIdJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetParticipantBalanceByCurrencyIdJdbcQueryHandler(
            @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws HubServicesException {

        ParticipantBalanceData result;
        TableNames t = new TableNames(jdbcTemplate);

        try {
            //@@Formatter:off
            String sql = """
                      SELECT
                        pc.currencyId AS currency,
                        la.name AS ledgerAccountType,
                        pp.`value`,
                        pp.reservedValue,
                        pc.isActive,
                        pp.changedDate
                      FROM `%s` pp
                      INNER JOIN `%s` pc ON pc.participantCurrencyId = pp.participantCurrencyId
                      LEFT JOIN `%s` la ON la.ledgerAccountTypeId = pc.ledgerAccountTypeId
                      WHERE pp.participantCurrencyId = ?
                    """.formatted(t.pp(), t.pc(), t.la());

            //@@Formatter:on
            result = this.jdbcTemplate.queryForObject(sql,
                                                      new ParticipantBalanceDataMapper(),
                                                      input.getParticipantCurrencyId());

        } catch (Exception e) {

            throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_BALANCE_ERROR.description(e.getMessage()));
        }

        return new Output(result);
    }

    public final class TableNames {

        private final String pp;

        private final String pc;

        private final String la;

        public TableNames(JdbcTemplate jdbcTemplate) {

            this.pp = resolve(jdbcTemplate, "participantposition", "participantPosition");
            this.pc = resolve(jdbcTemplate, "participantcurrency", "participantCurrency");
            this.la = resolve(jdbcTemplate, "ledgeraccounttype", "ledgerAccountType");
        }

        public String pp() {return pp;}

        public String pc() {return pc;}

        public String la() {return la;}

        private static String resolve(JdbcTemplate jdbc, String... candidates) {

            String placeholders = String.join(",", Collections.nCopies(candidates.length, "?"));
            String sql = """
                    SELECT TABLE_NAME
                    FROM information_schema.TABLES
                    WHERE TABLE_SCHEMA = DATABASE()
                      AND TABLE_NAME IN (""" + placeholders + ")";
            List<String> found = jdbc.queryForList(sql, String.class, (Object[]) candidates);

            if (found.isEmpty()) {

                throw new IllegalStateException("None of the expected table names exist: " +
                                                        Arrays.stream(candidates).collect(Collectors.joining(", ")));
            }

            for (String c : candidates) {if (found.contains(c)) {return c;}}

            return found.get(0);
        }

    }

}
