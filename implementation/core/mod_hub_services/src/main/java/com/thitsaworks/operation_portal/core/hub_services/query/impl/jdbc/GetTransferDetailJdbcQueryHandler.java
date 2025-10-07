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
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TransferDetailData execute(String transferId, String timeZone) throws HubServicesException {

        List<TransferDetailData> result;

        //@@Formatter:off
        final String query = """
                 SELECT  t.transferId AS transferId,
                 IFNULL(q.quoteId,'') AS quoteId,
                 IFNULL(tst.enumeration,'') AS transferState,
                 IFNULL(ts.name,'') AS transferType,
                 IFNULL(ss.name,'') AS subScenario,
                 IFNULL(t.currencyId,'') AS currency,
                 IFNULL(at.name,'') AS amountType,
                 IF(q.amount IS NOT NULL, FORMAT(ROUND(q.amount, 2), 2), '') AS quoteAmount,
                 IF(t.amount IS NOT NULL, FORMAT(ROUND(t.amount, 2), 2), '') AS transferAmount,
                 IF(tppayee.amount IS NOT NULL, FORMAT(ROUND(tppayee.amount, 2), 2), '') AS payeeReceivedAmount,
                 IF(qres.payeeFspFeeAmount IS NOT NULL, FORMAT(ROUND(qres.payeeFspFeeAmount, 2), 2), '') AS payeeDfspFeeAmount,
                 IF(qres.payeeFspCommissionAmount IS NOT NULL, FORMAT(ROUND(qres.payeeFspCommissionAmount, 2), 2), '') AS payeeDfspCommissionAmount,
                 IFNULL(t.createdDate,'') AS submittedOnDate,
                 IFNULL(tff.settlementWindowId ,'') AS windowId,
                 IFNULL(swc.settlementId,'') AS settlementId,
                
                 IFNULL(idenpayer.name,'') as payerIdType,
                 IFNULL(payerv.partyIdentifierValue,'') as payerIdValue,
                 IFNULL(payer.name,'') AS payerDspId,
                 CONCAT(
                     IFNULL(ppayer.firstName, ''),
                     IF(ppayer.firstName IS NOT NULL AND ppayer.firstName != '' AND ppayer.middleName IS NOT NULL AND ppayer.middleName != '', ' ', ''),
                     IFNULL(ppayer.middleName, ''),
                     IF((ppayer.firstName IS NOT NULL AND ppayer.firstName != '') OR (ppayer.middleName IS NOT NULL AND ppayer.middleName != ''), ' ', ''),
                     IFNULL(ppayer.lastName, '')
                 ) AS payerName,
                
                
                 IFNULL(idenpayee.name,'') as payeeIdType,
                 IFNULL(payeev.partyIdentifierValue,'') as payeeIdValue,
                 IFNULL(payee.name,'') AS payeeDspId,
                 CONCAT(
                     IFNULL(ppayee.firstName, ''),
                     IF(ppayee.firstName IS NOT NULL AND ppayee.firstName != '' AND ppayee.middleName IS NOT NULL AND ppayee.middleName != '', ' ', ''),
                     IFNULL(ppayee.middleName, ''),
                     IF((ppayee.firstName IS NOT NULL AND ppayee.firstName != '') OR (ppayee.middleName IS NOT NULL AND ppayee.middleName != ''), ' ', ''),
                     IFNULL(ppayee.lastName, '')
                 ) AS payeeName,
                
                 COALESCE(te.errorCode, qe.errorCode, '') AS errorCode,
                 COALESCE(te.errorDescription, qe.errorDescription, '') AS errorDescription
                
                FROM transfer t
                LEFT JOIN (SELECT MAX(transferStateChangeId) AS transferStateChangeId,  transferId  FROM transferStateChange tsa GROUP BY transferId) tsc ON t.transferId= tsc.transferId
                LEFT JOIN transferStateChange tss ON tss.transferStateChangeId=tsc.transferStateChangeId
                LEFT JOIN transferFulfilment tff ON tff.transferId = t.transferId
                LEFT JOIN transferState tst ON tst.transferStateId= tss.transferStateId
                LEFT JOIN settlementWindowContent swc ON swc.settlementWindowId = tff.settlementWindowId
                
                LEFT JOIN quote q ON q.transactionReferenceId = t.transferId
                LEFT JOIN quoteResponse qres ON qres.quoteId = q.quoteId
                LEFT JOIN amountType at ON at.amountTypeId = q.amountTypeId
                LEFT JOIN transactionScenario ts ON ts.transactionScenarioId = q.transactionScenarioId
                LEFT JOIN transactionsubscenario ss ON ss.transactionSubScenarioId = q.transactionSubScenarioId
                
                LEFT JOIN transferParticipant tppayer ON t.transferId = tppayer.transferId AND tppayer.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYER_DFSP')
                LEFT JOIN participantCurrency payercurrency ON payercurrency.participantCurrencyId = tppayer.participantCurrencyId
                LEFT JOIN participant payer ON payer.participantId = payercurrency.participantId
                LEFT JOIN quoteParty payerv ON q.quoteId = payerv.quoteId AND payerv.fspId = payer.name  AND payerv.partyTypeId = (SELECT partyTypeId FROM partyType WHERE NAME='PAYER')
                LEFT JOIN party ppayer ON ppayer.quotePartyId = payerv.quotePartyId AND payerv.fspId = payer.name
                LEFT JOIN partyIdentifierType idenpayer ON payerv.partyIdentifierTypeId= idenpayer.partyIdentifierTypeId
                
                LEFT JOIN transferParticipant tppayee ON t.transferId = tppayee.transferId AND tppayee.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYEE_DFSP')
                LEFT JOIN participantCurrency payeecurrency ON payeecurrency.participantCurrencyId = tppayee.participantCurrencyId
                LEFT JOIN participant payee ON payee.participantId = payeecurrency.participantId
                LEFT JOIN quoteParty payeev ON q.quoteId = payeev.quoteId AND payeev.fspId = payee.name AND payeev.partyTypeId =  (SELECT partyTypeId FROM partyType WHERE NAME='PAYEE')
                LEFT JOIN party ppayee ON ppayee.quotePartyId = payeev.quotePartyId AND payeev.fspId = payee.name
                LEFT JOIN partyIdentifierType idenpayee ON payeev.partyIdentifierTypeId = idenpayee.partyIdentifierTypeId
                
                LEFT JOIN transferError te ON te.transferId = t.transferId
                LEFT JOIN quoteError qe ON qe.quoteId = q.quoteId
                
                WHERE  t.transferId = IFNULL(?,t.transferId);
                """;
        //@@Formatter:on

        try {

            result = this.jdbcTemplate.query(query
                , new TransferDetailDataMapper(),
                transferId);

        } catch (Exception e) {
            throw new HubServicesException(HubServicesErrors.HUB_TRANSFER_ERROR.description(e.getMessage()));
        }

        if (result.isEmpty()) {
            throw new HubServicesException(HubServicesErrors.HUB_TRANSFER_ERROR.description(
                    "Transfer with Id [" + transferId + "] cannot find on Hub"));
        }

        return result.getFirst();

    }

}
