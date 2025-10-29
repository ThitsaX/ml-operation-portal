package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.ModifyParticipantDescriptionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ModifyParticipantDescriptionJpaQueryHandler implements ModifyParticipantDescriptionQuery {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantDescriptionJpaQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ModifyParticipantDescriptionJpaQueryHandler(
        @Qualifier(PersistenceQualifiers.Hub.WRITE_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws HubServicesException {

        try {

            //@@formater:off
            String sql = "UPDATE participant \n" +
                             "SET description = ? \n" +
                             "WHERE name = ?";
            //@@formater:on

            this.jdbcTemplate.update(sql,
                                     input.description(),
                                     input.participantName());

        } catch (Exception e) {

            LOG.error("Error : [{}]", e.getMessage());

            throw new HubServicesException(HubServicesErrors.HUB_PARTICIPANT_DESCRIPTION_ERROR.description(e.getMessage()));
        }

        return new Output(true);
    }

}
