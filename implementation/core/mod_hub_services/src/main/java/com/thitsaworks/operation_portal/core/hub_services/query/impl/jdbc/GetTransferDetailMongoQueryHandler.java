package com.thitsaworks.operation_portal.core.hub_services.query.impl.jdbc;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferDetailData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetTransferDetailQuery;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Primary
public class GetTransferDetailMongoQueryHandler implements GetTransferDetailQuery {

    private static final String COLLECTION = "transaction";

    private final MongoTemplate reportingMongoReadTemplate;

    @Autowired
    public GetTransferDetailMongoQueryHandler(
            @Qualifier(PersistenceQualifiers.Reporting.MONGO_READ_TEMPLATE) MongoTemplate reportingMongoReadTemplate) {

        this.reportingMongoReadTemplate = reportingMongoReadTemplate;
    }

    @Override
    public TransferDetailData execute(String transferId) throws HubServicesException {

        final List<Document> pipeline = new ArrayList<>();

        // WHERE t.transferId = IFNULL(?, t.transferId)
        if (StringUtils.hasText(transferId)) {
            pipeline.add(new Document("$match", new Document("transferId", transferId)));
        }

        // LEFT JOIN settlement on transferSettlementWindowId ∈ settlement.settlementWindows[].settlementWindowId
        //   This matches the sample docs you shared.
        Document lookupSettlement = new Document("$lookup", new Document().append("from", "settlement")
                                                                          .append("let",
                                                                                  new Document("wId",
                                                                                               new Document("$toLong",
                                                                                                            "$transferSettlementWindowId")))
                                                                          .append("pipeline",
                                                                                  Arrays.asList(new Document("$match",
                                                                                                             new Document(
                                                                                                                     "$expr",
                                                                                                                     new Document(
                                                                                                                             "$in",
                                                                                                                             Arrays.asList(
                                                                                                                                     "$$wId",
                                                                                                                                     new Document(
                                                                                                                                             "$map",
                                                                                                                                             new Document().append(
                                                                                                                                                                   "input",
                                                                                                                                                                   "$settlementWindows")
                                                                                                                                                           .append("as",
                                                                                                                                                                   "sw")
                                                                                                                                                           .append("in",
                                                                                                                                                                   new Document(
                                                                                                                                                                           "$toLong",
                                                                                                                                                                           "$$sw.settlementWindowId"))))))),
                                                                                                new Document("$project",
                                                                                                             new Document(
                                                                                                                     "_id",
                                                                                                                     0).append(
                                                                                                                     "settlementId",
                                                                                                                     1))))
                                                                          .append("as", "settle"));
        pipeline.add(lookupSettlement);
        pipeline.add(new Document("$unwind",
                                  new Document("path", "$settle").append("preserveNullAndEmptyArrays", true)));

        // Final projection – keep raw values; we'll format amounts in Java
        Document project = new Document("$project", new Document().append("_id", 0)

                                                                  // IDs / states / types
                                                                  .append("transferId", "$transferId")
                                                                  .append("quoteId",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList(
                                                                                               "$quoteRequest.quoteId",
                                                                                               "")))
                                                                  .append("transferState",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList("$transferState",
                                                                                                     "")))
                                                                  .append("transferType",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList("$baseUseCase",
                                                                                                     "")))
                                                                  .append("currency",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList("$sourceCurrency",
                                                                                                     "")))
                                                                  .append("amountType",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList(
                                                                                               "$quoteRequest.amountType",
                                                                                               "")))

                                                                  // Amounts (leave numeric/null; we'll format to 2dp strings in Java)
                                                                  .append("quoteAmount", "$quoteRequest.amount.amount")
                                                                  .append("transferAmount",
                                                                          "$transferTerms.transferAmount.amount")
                                                                  .append("payeeReceivedAmount",
                                                                          "$transferTerms.payeeReceiveAmount.amount")
                                                                  .append("payeeDfspFeeAmount",
                                                                          "$transferTerms.payeeFspFee.amount")
                                                                  .append("payeeDfspCommissionAmount",
                                                                          "$transferTerms.payeeFspCommission.amount")

                                                                  // Dates / windows / settlements
                                                                  .append("submittedOnDate", "$createdAt")
                                                                  .append("windowId",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList(
                                                                                               "$transferSettlementWindowId",
                                                                                               "")))
                                                                  .append("settlementId",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList(
                                                                                               "$settle.settlementId",
                                                                                               "")))

                                                                  // Payer
                                                                  .append("payerIdType",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList(
                                                                                               "$payerParty.partyIdType",
                                                                                               "")))
                                                                  .append("payerIdValue",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList(
                                                                                               "$payerParty.partyIdentifier",
                                                                                               "")))
                                                                  .append("payerDspId",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList("$payerDFSP", "")))
                                                                  .append("payerName",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList(
                                                                                               "$payerParty.partyName",
                                                                                               "")))

                                                                  // Payee
                                                                  .append("payeeIdType",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList(
                                                                                               "$payeeParty.partyIdType",
                                                                                               "")))
                                                                  .append("payeeIdValue",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList(
                                                                                               "$payeeParty.partyIdentifier",
                                                                                               "")))
                                                                  .append("payeeDspId",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList("$payeeDFSP", "")))
                                                                  .append("payeeName",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList(
                                                                                               "$payeeParty.partyName",
                                                                                               "")))

                                                                  // Errors
                                                                  .append("errorCode",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList("$errorCode", "")))
                                                                  .append("errorDescription",
                                                                          new Document("$ifNull",
                                                                                       Arrays.asList("$errorDescription",
                                                                                                     ""))));
        pipeline.add(project);

        // Execute against the `transfer` collection
        MongoCollection<Document> col = reportingMongoReadTemplate.getDb().getCollection("transfer");
        AggregateIterable<Document> iterable = col.aggregate(pipeline).allowDiskUse(true);

        TransferDetailData out = null;

        for (Document d : iterable) {
            out = TransferProjection.from(d);
        }
        return out;
    }

    public static class TransferProjection {

        private static final DecimalFormat TWO_DP = new DecimalFormat("0.00");

        public static TransferDetailData from(Document d) {

            TransferDetailData.PartyInfoData payerInformation =
                    new TransferDetailData.PartyInfoData(d.get("payerIdType").toString(),
                                                         d.get("payerIdValue").toString(),
                                                         d.get("payerDspId").toString(),
                                                         d.get("payerName").toString());

            TransferDetailData.PartyInfoData payeeInformation =
                    new TransferDetailData.PartyInfoData(d.get("payeeIdType").toString(),
                                                         d.get("payeeIdValue").toString(),
                                                         d.get("payeeDspId").toString(),
                                                         d.get("payeeName").toString());

            TransferDetailData.TransferErrorInfo errorInfo = new TransferDetailData.TransferErrorInfo(d.get("errorCode")
                                                                                                       .toString(),
                                                                                                      d.get("errorDescription")
                                                                                                       .toString());

            return new TransferDetailData(d.get("transferId").toString(),
                                          d.get("quoteId").toString(),
                                          d.get("transferState").toString(),
                                          d.get("transferType").toString(),
                                          d.get("currency").toString(),
                                          d.get("amountType").toString(),
                                          d.get("quoteAmount").toString(),
                                          d.get("transferAmount").toString(),
                                          d.get("payeeReceivedAmount").toString(),
                                          d.get("payeeDfspFeeAmount").toString(),
                                          d.get("payeeDfspCommissionAmount").toString(),
                                          d.get("submittedOnDate").toString(),
                                          d.get("windowId").toString(),
                                          d.get("settlementId").toString(),
                                          payerInformation,
                                          payeeInformation,
                                          errorInfo);
        }

        private static String str(Object v) {

            if (v == null) {return "";}
            if (v instanceof String) {return (String) v;}
            return String.valueOf(v);
        }

        private static String money(Object v) {

            if (v == null) {return "";}
            if (v instanceof Number) {
                // Round/format to 2dp
                return TWO_DP.format(((Number) v).doubleValue());
            }
            // If already a string, pass through
            return Objects.toString(v, "");
        }

    }

}