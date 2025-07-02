package com.thitsaworks.operation_portal.reporting.central_ledger.query.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.mapper.TransferDataMapper;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetTransfers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GetTransfersHandler implements GetTransfers {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransfersHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetTransfersHandler(@Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws Exception {

        var results = this.jdbcTemplate.query(
                "SELECT  t.transferId AS transferId, IFNULL(tst.enumeration,'') AS state, IFNULL(ts.name,'') AS type,\n" +
                        "    IFNULL(t.currencyId,'') AS currency, ROUND(t.amount,2) AS amount, IFNULL(payer.name,'') AS payer_dfsp,\n" +
                        "    IFNULL(payee.name,'') AS payee_dfsp, IFNULL(swc.settlementId,'') AS settlement_batch," +
                        " t.createdDate AS submitted_on_date \n" +
                        "FROM transfer t \n" +
                        "LEFT JOIN transferParticipant tppayer ON t.transferId = tppayer.transferId AND tppayer.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYER_DFSP')\n" +
                        "LEFT JOIN participantCurrency payercurrency ON payercurrency.participantCurrencyId = tppayer.participantCurrencyId\n" +
                        "LEFT JOIN participant payer ON payer.participantId = payercurrency.participantId\n" +
                        "LEFT JOIN transferParticipant tppayee ON t.transferId = tppayee.transferId AND tppayee.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYEE_DFSP')\n" +
                        "LEFT JOIN participantCurrency payeecurrency ON payeecurrency.participantCurrencyId = tppayee.participantCurrencyId\n" +
                        "LEFT JOIN participant payee ON payee.participantId = payeecurrency.participantId\n" +
                        "LEFT JOIN quote q ON q.transactionReferenceId = t.transferId\n" +
                        "LEFT JOIN quoteParty payerv ON q.quoteId = payerv.quoteId AND payerv.fspId = payer.name AND payerv.partyTypeId = (SELECT partyTypeId FROM partyType WHERE NAME='PAYER') \n" +
                        "LEFT JOIN quoteParty payeev ON q.quoteId = payeev.quoteId AND payeev.fspId = payee.name  AND payeev.partyTypeId =  (SELECT partyTypeId FROM partyType WHERE NAME='PAYEE') \n" +
                        "LEFT JOIN partyIdentifierType idenpayer ON payerv.partyIdentifierTypeId= idenpayer.partyIdentifierTypeId \n" +
                        "LEFT JOIN partyIdentifierType idenpayee ON payeev.partyIdentifierTypeId= idenpayee.partyIdentifierTypeId \n" +
                        "LEFT JOIN transactionScenario ts ON ts.transactionScenarioId = q.transactionScenarioId\n" +
                        "LEFT JOIN (SELECT MAX(transferStateChangeId) AS transferStateChangeId,  transferId  FROM transferStateChange tsa GROUP BY transferId) tsc ON t.transferId= tsc.transferId \n" +
                        "LEFT JOIN transferStateChange tss ON tss.transferStateChangeId=tsc.transferStateChangeId \n" +
                        "LEFT JOIN transferFulfilment tff ON tff.transferId = t.transferId \n" +
                        "LEFT JOIN transferState tst ON tst.transferStateId= tss.transferStateId \n" +
                        "LEFT JOIN settlementWindowContent swc ON swc.settlementWindowId = tff.settlementWindowId \n" +
                        "AND swc.currencyId = t.currencyId \n" +
                        "WHERE 1=1 \n" +
                        " AND t.createdDate  BETWEEN ? AND ? \n" +
                        " AND t.transferId = IFNULL(?,t.transferId) \n" +
                        " AND payerv.fspId = IFNULL(?,payerv.fspId) \n" +
                        " AND payeev.fspId = IFNULL(?,payeev.fspId) \n" +
                        " AND idenpayer.name = IFNULL(?,idenpayer.name) \n" +
                        " AND idenpayee.name = IFNULL(?,idenpayee.name) \n" +
                        " AND payerv.partyIdentifierValue = IFNULL(?,payerv.partyIdentifierValue) \n" +
                        " AND payeev.partyIdentifierValue = IFNULL(?,payeev.partyIdentifierValue) \n" +
                        " AND t.currencyId = IFNULL(?, t.currencyId)\n" +
                        " AND tst.transferStateId IN (SELECT DISTINCT transferStateId FROM transferState WHERE enumeration = IFNULL(?,enumeration))\n" +
                        " AND (payer.name = IFNULL(?, payer.name) OR payee.name = IFNULL(?, payee.name))  ORDER BY  t.createdDate DESC;",
                new TransferDataMapper(), input.getFromDate(), input.getToDate() , input.getTransferId(),
                input.getPayerFspId(), input.getPayeeFspId(), input.getPayerIdentifierTypeId(), input.getPayeeIdentifierTypeId(),
                input.getPayerIdentifierValue(), input.getPayeeIdentifierValue(),
                input.getCurrencyId(), input.getTransferStateId(), input.getFspName(), input.getFspName());

        if (results == null || results.isEmpty()) {

            return new Output(new ArrayList<>());
        }

        return new Output(results);
    }

}
