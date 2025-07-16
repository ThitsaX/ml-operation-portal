package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferDetailData;
import com.thitsaworks.operation_portal.core.hub_services.data.mapper.TransferDetailDataMapper;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetTransferDetailQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTransferDetailJdbcQueryHandler implements GetTransferDetailQuery {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransferDetailJdbcQueryHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GetTransferDetailJdbcQueryHandler(
        @Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TransferDetailData execute(String transferId) throws HubServicesException {

        List<TransferDetailData> result;

        String query = """
                 SELECT  t.transferId AS transferId, 
                 q.quoteId AS quoteId, 
                 IFNULL(tst.enumeration,'') AS transferState, 
                 IFNULL(ts.name,'') AS transferType, 
                 IFNULL(t.currencyId,'') AS currency,
                 IFNULL(at.name,'') AS amountType, 
                 ROUND(q.amount,2) AS quoteAmount,
                 ROUND(t.amount,2) AS transferAmount, 
                 ROUND(tppayee.amount,2) AS payeeReceivedAmount, 
                 ROUND(qres.payeeFspFeeAmount,2) AS payeeDfspFeeAmount, 
                 ROUND(qres.payeeFspCommissionAmount,2) AS payeeDfspCommissionAmount, 
                 IFNULL(tff.settlementWindowId ,'') AS windowId, 
                 IFNULL(swc.settlementId,'') AS settlementId, 
                 t.createdDate AS submitted_on_date,  
                
                 idenpayer.name as payerIdType,
                 payerv.partyIdentifierValue as payerIdValue,
                 IFNULL(payer.name,'') AS payerDspId, 
                 CONCAT(IFNULL(ppayer.firstName,'') , ' ' , IFNULL(ppayer.middleName,'') , ' ', IFNULL(ppayer.lastName,'')) AS payerName,
                
                 idenpayee.name as payeeIdType,
                 payeev.partyIdentifierValue as payeeIdValue,
                 IFNULL(payee.name,'') AS payeeDspId, 
                 CONCAT(IFNULL(ppayee.firstName,'') , ' ' , IFNULL(ppayee.middleName,'') , ' ', IFNULL(ppayee.lastName,'')) AS payeeName,
                
                 COALESCE(te.errorCode, qe.errorCode, '') AS errorCode,
                 COALESCE(te.errorDescription, qe.errorDescription, '') AS errorDescription
                
                FROM transfer t 
                LEFT JOIN transferParticipant tppayer ON t.transferId = tppayer.transferId AND tppayer.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYER_DFSP')
                LEFT JOIN participantCurrency payercurrency ON payercurrency.participantCurrencyId = tppayer.participantCurrencyId
                LEFT JOIN participant payer ON payer.participantId = payercurrency.participantId
                LEFT JOIN transferParticipant tppayee ON t.transferId = tppayee.transferId AND tppayee.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYEE_DFSP')
                LEFT JOIN participantCurrency payeecurrency ON payeecurrency.participantCurrencyId = tppayee.participantCurrencyId
                LEFT JOIN participant payee ON payee.participantId = payeecurrency.participantId
                LEFT JOIN quote q ON q.transactionReferenceId = t.transferId
                LEFT JOIN quoteResponse qres ON qres.quoteId = q.quoteId
                LEFT JOIN quoteParty payerv ON q.quoteId = payerv.quoteId AND payerv.fspId = payer.name  AND payerv.partyTypeId = (SELECT partyTypeId FROM partyType WHERE NAME='PAYER') 
                LEFT JOIN quoteParty payeev ON q.quoteId = payeev.quoteId AND payeev.fspId = payee.name AND payeev.partyTypeId =  (SELECT partyTypeId FROM partyType WHERE NAME='PAYEE') 
                LEFT JOIN party ppayer ON ppayer.quotePartyId = payerv.quotePartyId AND payerv.fspId = payer.name 
                LEFT JOIN party ppayee ON ppayee.quotePartyId = payeev.quotePartyId AND payeev.fspId = payee.name 
                LEFT JOIN partyIdentifierType idenpayer ON payerv.partyIdentifierTypeId= idenpayer.partyIdentifierTypeId 
                LEFT JOIN partyIdentifierType idenpayee ON payeev.partyIdentifierTypeId= idenpayee.partyIdentifierTypeId 
                LEFT JOIN transactionScenario ts ON ts.transactionScenarioId = q.transactionScenarioId
                LEFT JOIN (SELECT MAX(transferStateChangeId) AS transferStateChangeId,  transferId  FROM transferStateChange tsa GROUP BY transferId) tsc ON t.transferId= tsc.transferId
                LEFT JOIN transferStateChange tss ON tss.transferStateChangeId=tsc.transferStateChangeId 
                LEFT JOIN transferFulfilment tff ON tff.transferId = t.transferId 
                LEFT JOIN transferState tst ON tst.transferStateId= tss.transferStateId 
                LEFT JOIN settlementWindowContent swc ON swc.settlementWindowId = tff.settlementWindowId 
                LEFT JOIN transferError te ON te.transferId = t.transferId
                LEFT JOIN quoteError qe ON qe.quoteId = q.quoteId
                LEFT JOIN amountType at ON at.amountTypeId = q.amountTypeId
                WHERE  t.transferId = IFNULL(?,t.transferId);
                """;

        try {
            //@@Formatter:off
                result = this.jdbcTemplate.query(query
                , new TransferDetailDataMapper(),
                transferId);

        //@@Formatter:on

        } catch (Exception e) {
            throw new HubServicesException(HubServicesErrors.CENTRAL_LEDGER_FAILURE_EXCEPTION);
        }

        if (result.isEmpty()) {
            throw new HubServicesException(HubServicesErrors.HUB_TRANSACTION_NOT_FOUND);
        }

        return result.getFirst();

    }

}
