package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowInfoData;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.SettlementWindowInfoDataMapper;
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
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws HubServicesException {

        List<SettlementWindowInfoData> results = null;

        try {
            results = this.jdbcTemplate.query(
                "SELECT  \n" +
                    "  p.name AS DfspName,\n" +
                    "  IF(SUM(tp.amount) < 0, SUM(tp.amount), 0) AS Debit,\n" +
                    "  IF(SUM(tp.amount) > 0, SUM(tp.amount), 0) AS Credit,\n" +
                    "  pc.currencyId,\n" +
                    "  swso.createdDate as WindowOpenDate,\n" +
                    "  swsf.createdDate as WindowSettledDate\n" +
                    "FROM \n" +
                    "  central_ledger.transferFulfilment tf\n" +
                    "JOIN \n" +
                    "  transferParticipant tp ON tf.transferId = tp.transferId\n" +
                    "JOIN \n" +
                    "  participant p ON tp.participantId = p.participantId\n" +
                    "JOIN \n" +
                    "  participantCurrency pc ON tp.participantCurrencyId = pc.participantCurrencyId\n" +
                    "Join \n" +
                    " settlementWindowStateChange swso on swso.settlementWindowId = tf.settlementWindowId\n" +
                    " AND swso.settlementWindowStateId ='OPEN' \n" +
                    " Join \n" +
                    " settlementWindowStateChange swsf on swsf.settlementWindowId = tf.settlementWindowId\n" +
                    " AND swsf.settlementWindowStateId ='SETTLED' \n" +
                    "WHERE \n" +
                    "  tf.settlementWindowId = ?\n" +
                    "GROUP BY \n" +
                    "  p.name, pc.currencyId ,swso.createdDate, swsf.createdDate;",
                new SettlementWindowInfoDataMapper(),
                input.getSettlementWindowId()
                                             );
        } catch (Exception e) {

            throw new HubServicesException(HubServicesErrors.SETTLEMENT_WINDOW_ERROR.description(e.getMessage()));
        }

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        return new Output(results);
    }

}
