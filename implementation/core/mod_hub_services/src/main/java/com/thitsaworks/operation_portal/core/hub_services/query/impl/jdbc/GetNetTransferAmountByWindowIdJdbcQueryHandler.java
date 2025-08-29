package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.WindowInfoData;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.WindowInfoDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetNetTransferAmountByWindowIdQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetNetTransferAmountByWindowIdJdbcQueryHandler implements GetNetTransferAmountByWindowIdQuery {

    private static final Logger LOG = LoggerFactory.getLogger(GetNetTransferAmountByWindowIdJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetNetTransferAmountByWindowIdJdbcQueryHandler(
        @Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws HubServicesException {

        List<WindowInfoData> results = null;

        try {

            //@@Formatter:off
            results = this.jdbcTemplate.query(
                "SELECT  \n" +
                    "  p.participantName AS DfspName,\n" +
                    "  SUM(CASE \n" +
                    "        WHEN tp.transferParticipantRoleTypeId = 1 THEN tp.amount\n" +
                    "        ELSE 0\n" +
                    "      END) AS Debit,\n" +
                    "  SUM(CASE \n" +
                    "        WHEN tp.transferParticipantRoleTypeId = 2 THEN tp.amount\n" +
                    "        ELSE 0\n" +
                    "      END) AS Credit,\n" +
                    "  pc.currencyId,\n" +
                    "  sw_open.createdDate AS windowOpenedDate,\n" +
                    "  sw_closed.createdDate AS windowClosedDate\n" +
                    "FROM \n" +
                    "  central_ledger.transferFulfilment tf\n" +
                    "JOIN \n" +
                    "  transferParticipant tp ON tf.transferId = tp.transferId\n" +
                    "JOIN \n" +
                    "  participant p ON tp.participantId = p.participantId\n" +
                    "JOIN \n" +
                    "  participantCurrency pc ON tp.participantCurrencyId = pc.participantCurrencyId\n" +
                    "LEFT JOIN \n" +
                    "  settlementwindowstatechange sw_open \n" +
                    "    ON tf.settlementWindowId = sw_open.settlementWindowId AND sw_open.settlementWindowStateId = 'OPEN'\n" +
                    "LEFT JOIN \n" +
                    "  settlementwindowstatechange sw_closed \n" +
                    "    ON tf.settlementWindowId = sw_closed.settlementWindowId AND sw_closed.settlementWindowStateId = 'CLOSED'\n" +
                    "WHERE \n" +
                    "  tf.settlementWindowId = ?\n" +
                    "GROUP BY \n" +
                    "  p.participantName, pc.currencyId, sw_open.createdDate, sw_closed.createdDate;",
                new WindowInfoDataMapper(),
                input.getSettlementWindowId());

            //@@Formatter:on
        } catch (Exception e) {

            throw new HubServicesException(HubServicesErrors.CENTRAL_LEDGER_FAILURE_EXCEPTION);
        }

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        return new Output(results);
    }

}
