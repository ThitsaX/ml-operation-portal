package com.thitsaworks.operation_portal.reporting.central_ledger.query.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.BusinessData;
import com.thitsaworks.operation_portal.reporting.central_ledger.data.mapper.BusinessDataMapper;
import com.thitsaworks.operation_portal.reporting.central_ledger.exception.CentralLedgerErrors;
import com.thitsaworks.operation_portal.reporting.central_ledger.exception.CentralLedgerException;
import com.thitsaworks.operation_portal.reporting.central_ledger.query.GetTransferDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTransferDetailHandler implements GetTransferDetail {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferDetailHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetTransferDetailHandler(
        @Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws CentralLedgerException {

        List<BusinessData> result;

        try {
            //@@Formatter:off
                result = this.jdbcTemplate.query(
                "SELECT  t.transferId AS transferId, IFNULL(tst.enumeration,'') AS state, IFNULL(ts.name,'') AS type, \n" +
                        " IFNULL(t.currencyId,'') AS currency,ROUND( t.amount,2 )AS amount, \n" +
                        " CONCAT(IFNULL(ppayer.firstName,'') , ' ' , IFNULL(ppayer.middleName,'') , ' ', IFNULL(ppayer.lastName,'')) AS payer,\n" +
                        " CONCAT(idenpayer.name ,' ' , payerv.partyIdentifierValue) AS payer_details,  IFNULL(payer.name,'') AS payer_dfsp, \n" +
                        " CONCAT(IFNULL(ppayee.firstName,'') , ' ' , IFNULL(ppayee.middleName,'') , ' ', IFNULL(ppayee.lastName,'')) AS payee, \n" +
                        " CONCAT(idenpayee.name ,' ', payeev.partyIdentifierValue) AS payee_details, IFNULL(payee.name,'') AS payee_dfsp, \n" +
                        " IFNULL(swc.settlementId,'') AS settlement_batch, t.createdDate AS submitted_on_date  \n" +
                        "FROM transfer t \n" +
                        "LEFT JOIN transferParticipant tppayer ON t.transferId = tppayer.transferId AND tppayer.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYER_DFSP')\n" +
                        "LEFT JOIN participantCurrency payercurrency ON payercurrency.participantCurrencyId = tppayer.participantCurrencyId\n" +
                        "LEFT JOIN participant payer ON payer.participantId = payercurrency.participantId\n" +
                        "LEFT JOIN transferParticipant tppayee ON t.transferId = tppayee.transferId AND tppayee.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYEE_DFSP')\n" +
                        "LEFT JOIN participantCurrency payeecurrency ON payeecurrency.participantCurrencyId = tppayee.participantCurrencyId\n" +
                        "LEFT JOIN participant payee ON payee.participantId = payeecurrency.participantId\n" +
                        "LEFT JOIN quote q ON q.transactionReferenceId = t.transferId\n" +
                        "LEFT JOIN quoteParty payerv ON q.quoteId = payerv.quoteId AND payerv.fspId = payer.name  AND payerv.partyTypeId = (SELECT partyTypeId FROM partyType WHERE NAME='PAYER') \n" +
                        "LEFT JOIN quoteParty payeev ON q.quoteId = payeev.quoteId AND payeev.fspId = payee.name AND payeev.partyTypeId =  (SELECT partyTypeId FROM partyType WHERE NAME='PAYEE') \n" +
                        "LEFT JOIN party ppayer ON ppayer.quotePartyId = payerv.quotePartyId AND payerv.fspId = payer.name \n" +
                        "LEFT JOIN party ppayee ON ppayee.quotePartyId = payeev.quotePartyId AND payeev.fspId = payee.name \n" +
                        "LEFT JOIN partyIdentifierType idenpayer ON payerv.partyIdentifierTypeId= idenpayer.partyIdentifierTypeId \n" +
                        "LEFT JOIN partyIdentifierType idenpayee ON payeev.partyIdentifierTypeId= idenpayee.partyIdentifierTypeId \n" +
                        "LEFT JOIN transactionScenario ts ON ts.transactionScenarioId = q.transactionScenarioId\n" +
                        "LEFT JOIN (SELECT MAX(transferStateChangeId) AS transferStateChangeId,  transferId  FROM transferStateChange tsa GROUP BY transferId) tsc ON t.transferId= tsc.transferId\n" +
                        "LEFT JOIN transferStateChange tss ON tss.transferStateChangeId=tsc.transferStateChangeId \n" +
                        "LEFT JOIN transferFulfilment tff ON tff.transferId = t.transferId \n" +
                        "LEFT JOIN transferState tst ON tst.transferStateId= tss.transferStateId \n" +
                        "LEFT JOIN settlementWindowContent swc ON swc.settlementWindowId = tff.settlementWindowId \n" +
                        "WHERE  t.transferId = IFNULL(?,t.transferId);", new BusinessDataMapper(),
                input.getTransferId());

        //@@Formatter:on

        } catch (Exception e) {
            throw new CentralLedgerException(CentralLedgerErrors.CENTRAL_LEDGER_FAILURE_EXCEPTION);
        }

        if (result == null || result.isEmpty()) {
            return null;
        }

        return new GetTransferDetail.Output(result.get(0));
    }

}
