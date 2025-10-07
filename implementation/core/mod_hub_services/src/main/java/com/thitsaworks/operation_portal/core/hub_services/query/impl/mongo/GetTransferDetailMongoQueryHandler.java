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
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@Primary
public class GetTransferDetailMongoQueryHandler implements GetTransferDetailQuery {

    private static final String COLLECTION = "transaction";

    private final MongoTemplate reportingMongoReadTemplate;

    private static final DateTimeFormatter WITH_OFFSET = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    @Autowired
    public GetTransferDetailMongoQueryHandler(
            @Qualifier(PersistenceQualifiers.Hub.MONGO_READ_TEMPLATE) MongoTemplate reportingMongoReadTemplate) {
        this.reportingMongoReadTemplate = reportingMongoReadTemplate;
    }

    @Override
    public TransferDetailData execute(String transferId, String timeZone) throws HubServicesException {

        try {

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
                             .and(ConditionalOperators.IfNull.ifNull("transactionTypeDetail.subScenario").then(""))
                             .as("subScenario")
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
                             .and("createdAt").as("submittedOnDate")
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
            return TransferProjection.from(d, timeZone);

        } catch (Exception e) {

            throw new HubServicesException(HubServicesErrors.HUB_TRANSFER_ERROR.defaultMessage(e.getMessage()));
        }
    }

    public static class TransferProjection {

        private static final DecimalFormat TWO_DP = new DecimalFormat("0.00");

        public static TransferDetailData from(Document d, String timeZone) {

            TransferDetailData.PartyInfoData payerInformation = new TransferDetailData.PartyInfoData(str(d.get(
                    "payerIdType")), str(d.get("payerIdValue")), str(d.get("payerDspId")), str(d.get("payerName")));

            TransferDetailData.PartyInfoData payeeInformation = new TransferDetailData.PartyInfoData(str(d.get(
                    "payeeIdType")), str(d.get("payeeIdValue")), str(d.get("payeeDspId")), str(d.get("payeeName")));

            TransferDetailData.TransferErrorInfo errorInfo = new TransferDetailData.TransferErrorInfo(str(d.get(
                    "errorCode")), str(d.get("errorDescription")));

            final Object submitted = d.get("submittedOnDate");

            final ZoneId zone = parseZoneFlexible(timeZone);

            final String submittedOnDate =
                    (submitted instanceof Date dateVal)
                            ? WITH_OFFSET.format(dateVal.toInstant().atZone(zone))
                            : String.valueOf(submitted);

            return new TransferDetailData(str(d.get("transferId")),
                                          str(d.get("quoteId")),
                                          str(d.get("transferState")),
                                          str(d.get("transferType")),
                                          str(d.get("subScenario")),
                                          str(d.get("currency")),
                                          str(d.get("amountType")),
                                          money(d.get("quoteAmount")),
                                          money(d.get("transferAmount")),
                                          money(d.get("payeeReceivedAmount")),
                                          money(d.get("payeeDfspFeeAmount")),
                                          money(d.get("payeeDfspCommissionAmount")),
                                          submittedOnDate,
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

    private static ZoneId parseZoneFlexible(String raw) {

        if (raw == null || raw.isBlank()) {return ZoneOffset.UTC;}
        String s = raw.trim();

        if (s.equalsIgnoreCase("Z") || s.equalsIgnoreCase("UTC") || s.equalsIgnoreCase("GMT")) {return ZoneOffset.UTC;}

        try {return ZoneId.of(s);} catch (Exception ignored) {}

        String sign = (s.startsWith("+") || s.startsWith("-")) ? s.substring(0, 1) : "+";
        String digits = s.startsWith("+") || s.startsWith("-") ? s.substring(1) : s;
        digits = digits.replace(":", "");

        if (digits.length() == 4) {
            int hh = Integer.parseInt(digits.substring(0, 2));
            int mm = Integer.parseInt(digits.substring(2, 4));
            return ZoneOffset.ofHoursMinutes(sign.equals("-") ? -hh : hh, sign.equals("-") ? -mm : mm);
        } else if (digits.length() == 2) {

            int hh = Integer.parseInt(digits);
            return ZoneOffset.ofHours(sign.equals("-") ? -hh : hh);
        } else {

            try {return ZoneOffset.of(s);} catch (DateTimeException e) {
                throw new IllegalArgumentException("Unrecognized timezone/offset: " + raw, e);
            }
        }

    }

}
