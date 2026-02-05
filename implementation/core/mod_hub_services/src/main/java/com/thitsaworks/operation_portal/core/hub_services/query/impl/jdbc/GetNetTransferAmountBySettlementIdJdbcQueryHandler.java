package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowInfoData;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.SettlementWindowInfoDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetNetTransferAmountBySettlementIdQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetNetTransferAmountBySettlementIdJdbcQueryHandler implements GetNetTransferAmountBySettlementIdQuery {

    private static final Logger LOG = LoggerFactory.getLogger(GetNetTransferAmountBySettlementIdJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetNetTransferAmountBySettlementIdJdbcQueryHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws HubServicesException {

        List<SettlementWindowInfoData> results = null;

        try {

//@@Formatter:off
            results = this.jdbcTemplate.query(
                "SELECT\n" +
                    "    p.name AS DfspName,\n" +
                    "    IF(SUM(tp.amount) < 0, SUM(tp.amount), 0) AS Debit,\n" +
                    "    IF(SUM(tp.amount) > 0, SUM(tp.amount), 0) AS Credit,\n" +
                    "    pc.currencyId,\n" +
                    "    pcc.participantCurrencyId AS participantSettlementCurrencyId,\n" +
                    "    pl.value AS participantLimit,\n" +
                    "    pp.value AS participantBalance,\n" +
                    "    MAX(ndc.ndc_percent) as ndcPercent,\n" +
                    "    (\n" +
                    "        SELECT GROUP_CONCAT(DISTINCT ssw2.settlementWindowId ORDER BY ssw2.settlementWindowId)\n" +
                    "        FROM central_ledger.settlementSettlementWindow ssw2\n" +
                    "        WHERE ssw2.settlementId =?\n" +
                    "    ) AS WindowIDs,\n" +
                    "    ssw.createdDate AS WindowOpenDate,\n" +
                    "    swsf.createdDate AS WindowSettledDate\n" +
                    "FROM central_ledger.transferFulfilment tf\n" +
                    "JOIN transferParticipant tp\n" +
                    "    ON tf.transferId = tp.transferId\n" +
                    "JOIN participant p\n" +
                    "    ON tp.participantId = p.participantId\n" +
                    "LEFT JOIN operation_portal.tbl_participant_ndc ndc\n" +
                    "    ON ndc.participant_name = p.name\n" +
                    "JOIN participantCurrency pc\n" +
                    "    ON tp.participantCurrencyId = pc.participantCurrencyId\n" +
                    "JOIN participantCurrency pcc\n" +
                    "    ON pc.participantId = pcc.participantId\n" +
                    "   AND pc.currencyId = pcc.currencyId\n" +
                    "   AND pcc.ledgerAccountTypeId = 2\n" +
                    "JOIN participantLimit pl\n" +
                    "    ON pl.participantCurrencyId = pc.participantCurrencyId\n" +
                    "   AND pl.isActive = 1\n" +
                    "LEFT JOIN participantCurrency pcr\n" +
                    "    ON pcr.participantId = p.participantId\n" +
                    "   AND pcr.currencyId = pc.currencyId\n" +
                    "   AND pcr.ledgerAccountTypeId = 2\n" +
                    "LEFT JOIN participantPosition pp\n" +
                    "    ON pp.participantCurrencyId = pcr.participantCurrencyId\n" +
                    "JOIN settlementSettlementWindow ssw\n" +
                    "    ON ssw.settlementWindowId = tf.settlementWindowId\n" +
                    "LEFT JOIN settlementWindowStateChange swsf\n" +
                    "    ON swsf.settlementWindowId = tf.settlementWindowId\n" +
                    "   AND swsf.settlementWindowStateId = 'SETTLED'\n" +
                    "WHERE ssw.settlementId = ?\n" +
                    "GROUP BY\n" +
                    "    p.name,\n" +
                    "    pc.currencyId,\n" +
                    "    ssw.createdDate,\n" +
                    "    swsf.createdDate,\n" +
                    "    pcc.participantCurrencyId,\n" +
                    "    pl.value,\n" +
                    "    pp.value;\n",
                new SettlementWindowInfoDataMapper(),
                input.getSettlementId(), // for subquery
                input.getSettlementId()  // for main WHERE
                                             );
//@@Formatter:on

        } catch (Exception e) {

            throw new HubServicesException(HubServicesErrors.SETTLEMENT_ERROR.description(e.getMessage()));
        }

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        return new Output(results);
    }

}
