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

        try {
            //@@Formatter:off
            final String query = """
                            SELECT pc.currencyId AS currency, la.name AS ledgeraccounttype, pp.value, pp.reservedValue, pc.isActive, pp.changedDate FROM participantposition pp
                            INNER JOIN participantcurrency pc ON pc.participantCurrencyId = pp.participantCurrencyId
                            LEFT JOIN ledgeraccounttype la ON la.ledgerAccountTypeId = pc.ledgerAccountTypeId
                            WHERE pp.participantCurrencyId = ?
                    """;
            //@@Formatter:on
            result = this.jdbcTemplate.queryForObject(query,
                                                      new ParticipantBalanceDataMapper(),
                                                      input.getParticipantCurrencyId());
        } catch (Exception e) {

            throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_BALANCE_ERROR.description(e.getMessage()));
        }

        return new Output(result);
    }

}
