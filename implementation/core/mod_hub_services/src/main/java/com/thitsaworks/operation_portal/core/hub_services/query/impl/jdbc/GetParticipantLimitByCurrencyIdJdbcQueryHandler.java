package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.ParticipantBalanceData;
import com.thitsaworks.operation_portal.core.hub_services.data.ParticipantLimitData;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.ParticipantBalanceDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.ParticipantLimitDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantBalanceByCurrencyIdQuery;
import com.thitsaworks.operation_portal.core.hub_services.query.GetParticipantLimitByCurrencyIdQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class GetParticipantLimitByCurrencyIdJdbcQueryHandler implements GetParticipantLimitByCurrencyIdQuery {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantLimitByCurrencyIdJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetParticipantLimitByCurrencyIdJdbcQueryHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws HubServicesException {

        ParticipantLimitData result;

        try {
            //@@Formatter:off
            final String query = """
                               SELECT pl.participantCurrencyId,pl.value, pl.isActive from participantLimit pl
                                                                                     JOIN participantCurrency pc
                                                                                     ON pc.participantCurrencyId = pl.participantCurrencyId
                                                                                     JOIN participant p
                                                                                     ON p.participantId= pc.participantId
                                                                                     WHERE pl.isActive=1 and p.name = ? and pc.currencyId = ?
                                """;
            //@@Formatter:on
            result = this.jdbcTemplate.queryForObject(query,
                                                      new ParticipantLimitDataMapper(),
                                                      input.getParticipantName(),
                                                      input.getCurrencyId());

        } catch (Exception e) {

            throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_BALANCE_ERROR.description(e.getMessage()));
        }

        return new Output(result);
    }

}
