package com.thitsaworks.operation_portal.core.hub_services.query.impl.mongo;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferDetailData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetTransferDetailQuery;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.TimeZone;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

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

        try {

            final String tz = "UTC";
            final TimeZone tzObj = TimeZone.getTimeZone(tz);

            // 1) Normalize createdAt to a BSON Date (safe even if it's already a Date)
            AddFieldsOperation normalizeCreatedAt = AddFieldsOperation.builder()
                                                                      .addFieldWithValue("_createdAt",
                                                                                         ConvertOperators.ToDate.toDate(
                                                                                                 "$createdAt"))
                                                                      .build();

            Criteria root = Criteria.where("transferId").is(transferId);

            MatchOperation match = match(root);

            AggregationOperation lookupSettlement = context -> {
                Document stage = new Document("$lookup", new Document("from", "settlement").append("let",
                                                                                                   new Document("winId",
                                                                                                                new Document(
                                                                                                                        "$toString",
                                                                                                                        "$transferSettlementWindowId")))
                                                                                           .append("pipeline",
                                                                                                   Arrays.asList(
                                                                                                           // build array of windowIds as strings on the settlement docs
                                                                                                           new Document(
                                                                                                                   "$addFields",
                                                                                                                   new Document(
                                                                                                                           "_winIds",
                                                                                                                           new Document(
                                                                                                                                   "$map",
                                                                                                                                   new Document(
                                                                                                                                           "input",
                                                                                                                                           "$settlementWindows").append(
                                                                                                                                                                        "as",
                                                                                                                                                                        "w")
                                                                                                                                                                .append("in",
                                                                                                                                                                        new Document(
                                                                                                                                                                                "$toString",
                                                                                                                                                                                "$$w.settlementWindowId"))))),
                                                                                                           new Document(
                                                                                                                   "$match",
                                                                                                                   new Document(
                                                                                                                           "$expr",
                                                                                                                           new Document(
                                                                                                                                   "$in",
                                                                                                                                   Arrays.asList(
                                                                                                                                           "$$winId",
                                                                                                                                           "$_winIds")))),
                                                                                                           new Document(
                                                                                                                   "$project",
                                                                                                                   new Document(
                                                                                                                           "settlementId",
                                                                                                                           1))))
                                                                                           .append("as",
                                                                                                   "settlementMatches"));
                return context.getMappedObject(stage);
            };

            AddFieldsOperation addFirstSettlement = AddFieldsOperation.builder()
                                                                      .addFieldWithValue("settlementMatch",
                                                                                         new Document("$first",
                                                                                                      "$settlementMatches"))
                                                                      .build();

            ProjectionOperation project =
                    project().and("transferId")
                             .as("transferId")
                             .and(ConditionalOperators.IfNull.ifNull("quoteRequest.quoteId").then(""))
                             .as("quoteId")
                             .and(ConditionalOperators.IfNull.ifNull("transferState").then(""))
                             .as("transferState")
                             .and(ConditionalOperators.IfNull.ifNull("transactionType").then(""))
                             .as("transferType")
                             .and(ConditionalOperators.IfNull.ifNull("sourceCurrency").then(""))
                             .as("currency")
                             .and(ConditionalOperators.IfNull.ifNull("quoteRequest.amountType").then(""))
                             .as("amountType")
                             .and("quoteRequest.amount.amount")
                             .as("quoteAmount")
                             .and("transferTerms.transferAmount.amount")
                             .as("transferAmount")
                             .and("transferTerms.payeeReceiveAmount.amount")
                             .as("payeeReceivedAmount")
                             .and("transferTerms.payeeFspFee.amount")
                             .as("payeeDfspFeeAmount")
                             .and("transferTerms.payeeFspCommission.amount")
                             .as("payeeDfspCommissionAmount")
                             .and(
                                     DateOperators.DateToString.dateOf("$_createdAt")
                                                               .toString("%Y-%m-%d %H:%M:%S")
                                                               .withTimezone(DateOperators.Timezone.valueOf(tzObj.getID()))
                                 ).as("submittedOnDate")
                             .and(ConditionalOperators.IfNull.ifNull("transferSettlementWindowId").then(""))
                             .as("windowId")
                             .and(ConditionalOperators.IfNull.ifNull("$settlementMatch.settlementId").then(""))
                             .as("settlementId")
                             .and(ConditionalOperators.IfNull.ifNull("payerParty.partyIdType").then(""))
                             .as("payerIdType")
                             .and(ConditionalOperators.IfNull.ifNull("payerParty.partyIdentifier").then(""))
                             .as("payerIdValue")
                             .and(ConditionalOperators.IfNull.ifNull("payerDFSP").then(""))
                             .as("payerDspId")
                             .and(ConditionalOperators.IfNull.ifNull("payerParty.partyName").then(""))
                             .as("payerName")
                             .and(ConditionalOperators.IfNull.ifNull("payeeParty.partyIdType").then(""))
                             .as("payeeIdType")
                             .and(ConditionalOperators.IfNull.ifNull("payeeParty.partyIdentifier").then(""))
                             .as("payeeIdValue")
                             .and(ConditionalOperators.IfNull.ifNull("payeeDFSP").then(""))
                             .as("payeeDspId")
                             .and(ConditionalOperators.IfNull.ifNull("payeeParty.partyName").then(""))
                             .as("payeeName")
                             .and(ConditionalOperators.IfNull.ifNull("errorCode").then(""))
                             .as("errorCode")
                             .and(ConditionalOperators.IfNull.ifNull("errorDescription").then(""))
                             .as("errorDescription");

            Aggregation agg = newAggregation(match, lookupSettlement, addFirstSettlement, normalizeCreatedAt, project);

            AggregationResults<Document> results = reportingMongoReadTemplate.aggregate(agg,
                                                                                        COLLECTION,
                                                                                        Document.class);

            Document d = results.getUniqueMappedResult();

            if (d == null) {

                return null;
            }
            return TransferProjection.from(d);

        } catch (Exception e) {

            throw new HubServicesException(HubServicesErrors.CENTRAL_LEDGER_FAILURE_EXCEPTION);
        }
    }

    public static class TransferProjection {

        private static final DecimalFormat TWO_DP = new DecimalFormat("0.00");

        public static TransferDetailData from(Document d) {

            TransferDetailData.PartyInfoData payerInformation = new TransferDetailData.PartyInfoData(str(d.get(
                    "payerIdType")), str(d.get("payerIdValue")), str(d.get("payerDspId")), str(d.get("payerName")));

            TransferDetailData.PartyInfoData payeeInformation = new TransferDetailData.PartyInfoData(str(d.get(
                    "payeeIdType")), str(d.get("payeeIdValue")), str(d.get("payeeDspId")), str(d.get("payeeName")));

            TransferDetailData.TransferErrorInfo errorInfo = new TransferDetailData.TransferErrorInfo(str(d.get(
                    "errorCode")), str(d.get("errorDescription")));

            return new TransferDetailData(str(d.get("transferId")),
                                          str(d.get("quoteId")),
                                          str(d.get("transferState")),
                                          str(d.get("transferType")),
                                          str(d.get("currency")),
                                          str(d.get("amountType")),
                                          money(d.get("quoteAmount")),
                                          money(d.get("transferAmount")),
                                          money(d.get("payeeReceivedAmount")),
                                          money(d.get("payeeDfspFeeAmount")),
                                          money(d.get("payeeDfspCommissionAmount")),
                                          str(d.get("submittedOnDate")),
                                          str(d.get("windowId")),
                                          str(d.get("settlementId")),
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
                return TWO_DP.format(((Number) v).doubleValue());
            }
            return Objects.toString(v, "");
        }

    }

}
