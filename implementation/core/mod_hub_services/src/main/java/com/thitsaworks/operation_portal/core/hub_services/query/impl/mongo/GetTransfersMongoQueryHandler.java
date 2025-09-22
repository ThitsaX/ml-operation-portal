package com.thitsaworks.operation_portal.core.hub_services.query.impl.mongo;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesErrors;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.query.GetTransfersQuery;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@Primary
public class GetTransfersMongoQueryHandler implements GetTransfersQuery {

    private static final String COLLECTION = "transaction";

    private final MongoTemplate reportingMongoReadTemplate;

    @Autowired
    public GetTransfersMongoQueryHandler(
            @Qualifier(PersistenceQualifiers.Reporting.MONGO_READ_TEMPLATE) MongoTemplate reportingMongoReadTemplate) {

        this.reportingMongoReadTemplate = reportingMongoReadTemplate;
    }

    @Override
    public Output execute(Input input) throws HubServicesException {

        try {
            // 1) Parse required dates with timezone
            ZoneId zone = parseZoneOrDefault(input.getTimeZone(), ZoneOffset.UTC);
            Instant from = parseInstant(input.getFromDate(), zone);
            Instant to = parseInstantInclusiveEnd(input.getToDate(), zone);

            Date fromDate = Date.from(from);
            Date toDate = Date.from(to);

            // 2) Normalize createdAt to a BSON Date -> _createdAt
            AddFieldsOperation normalizeCreatedAt = AddFieldsOperation.builder()
                                                                      .addFieldWithValue("_createdAt",
                                                                                         ConvertOperators.ToDate.toDate(
                                                                                                 "$createdAt"))
                                                                      .build();

            // 3) Build match (append optional filters if present)
            Criteria root = Criteria.where("_createdAt").gte(fromDate).lte(toDate);

            if (StringUtils.isNotBlank(input.getTransferId())) {
                root = and(root, Criteria.where("transferId").is(input.getTransferId()));
            }

            if (StringUtils.isNotBlank(input.getFspName())) {
                root = and(root,
                           new Criteria().orOperator(Criteria.where("payerDFSP").is(input.getFspName()),
                                                     Criteria.where("payeeDFSP").is(input.getFspName())));
            }
            if (StringUtils.isNotBlank(input.getPayerFspId())) {
                root = and(root, Criteria.where("payerDFSP").is(input.getPayerFspId()));
            }
            if (StringUtils.isNotBlank(input.getPayeeFspId())) {
                root = and(root, Criteria.where("payeeDFSP").is(input.getPayeeFspId()));
            }
            if (StringUtils.isNotBlank(input.getPayerIdentifierTypeId())) {
                root = and(root, Criteria.where("payerParty.partyIdType").is(input.getPayerIdentifierTypeId()));
            }
            if (StringUtils.isNotBlank(input.getPayerIdentifierValue())) {
                root = and(root, Criteria.where("payerParty.partyIdentifier").is(input.getPayerIdentifierValue()));
            }
            if (StringUtils.isNotBlank(input.getPayeeIdentifierTypeId())) {
                root = and(root, Criteria.where("payeeParty.partyIdType").is(input.getPayeeIdentifierTypeId()));
            }
            if (StringUtils.isNotBlank(input.getPayeeIdentifierValue())) {
                root = and(root, Criteria.where("payeeParty.partyIdentifier").is(input.getPayeeIdentifierValue()));
            }
            if (StringUtils.isNotBlank(input.getCurrencyId())) {
                root = and(root, Criteria.where("transferTerms.transferAmount.currency").is(input.getCurrencyId()));
            }
            if (StringUtils.isNotBlank(input.getTransferStateId())) {
                root = and(root, Criteria.where("transferStateEnum").is(input.getTransferStateId()));
            }

            MatchOperation match = match(root);

            AggregationOperation lookupSettlement = context -> {
                Document stage = new Document("$lookup", new Document().append("from", "settlement")
                                                                       .append("let",
                                                                               new Document("winId",
                                                                                            new Document("$toString",
                                                                                                         "$transferSettlementWindowId")))
                                                                       .append("pipeline",
                                                                               Arrays.asList(new Document("$addFields",
                                                                                                          new Document(
                                                                                                                  "_winIds",
                                                                                                                  new Document(
                                                                                                                          "$map",
                                                                                                                          new Document().append(
                                                                                                                                                "input",
                                                                                                                                                "$settlementWindows")
                                                                                                                                        .append("as",
                                                                                                                                                "w")
                                                                                                                                        .append("in",
                                                                                                                                                new Document(
                                                                                                                                                        "$toString",
                                                                                                                                                        "$$w.settlementWindowId"))))),
                                                                                             new Document("$match",
                                                                                                          new Document(
                                                                                                                  "$expr",
                                                                                                                  new Document(
                                                                                                                          "$in",
                                                                                                                          Arrays.asList(
                                                                                                                                  "$$winId",
                                                                                                                                  "$_winIds")))),
                                                                                             new Document("$project",
                                                                                                          new Document(
                                                                                                                  "settlementId",
                                                                                                                  1))))
                                                                       .append("as", "settlementMatches"));
                return context.getMappedObject(stage);
            };

            AddFieldsOperation addFirstSettlement = AddFieldsOperation.builder()
                                                                      .addFieldWithValue("settlementMatch",
                                                                                         new Document("$first",
                                                                                                      "$settlementMatches"))
                                                                      .build();

            // 5) Project (use normalized _createdAt for submittedOnDate)
            ProjectionOperation project =
                    project().and("transferId")
                             .as("transferId")
                             .and(ConditionalOperators.IfNull.ifNull("transferStateEnum").then(""))
                             .as("state")
                             .and(ConditionalOperators.IfNull.ifNull("transactionType").then(""))
                             .as("type")
                             .and(ConditionalOperators.IfNull.ifNull("transferTerms.transferAmount.currency").then(""))
                             .as("currency")
                             .and(ArithmeticOperators.Round.roundValueOf("$transferTerms.transferAmount.amount")
                                                           .place(2))
                             .as("amount")
                             .and(ConditionalOperators.IfNull.ifNull("payerDFSP").then(""))
                             .as("payerDfsp")
                             .and(ConditionalOperators.IfNull.ifNull("payeeDFSP").then(""))
                             .as("payeeDfsp")
                             .and(ConditionalOperators.IfNull.ifNull("transferSettlementWindowId").then(""))
                             .as("windowId")
                             .and(ConditionalOperators.IfNull.ifNull("settlementMatch.settlementId").then(""))
                             .as("settlementBatch")
                             .and("_createdAt")
                             .as("submittedOnDate");

            SortOperation sort = sort(Sort.Direction.DESC, "submittedOnDate");

            Aggregation agg = newAggregation(normalizeCreatedAt,
                                             match,
                                             lookupSettlement,
                                             addFirstSettlement,
                                             project, sort);

            AggregationResults<Document> results = reportingMongoReadTemplate.aggregate(agg,
                                                                                        COLLECTION,
                                                                                        Document.class);

            // 6) Map to TransferData (replace with your real mapper)
            List<TransferData> list = new ArrayList<>();
            DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(zone);

            for (Document d : results) {
                String transferId = str(d.get("transferId"));
                String state = str(d.get("state"));
                String type = str(d.get("type"));
                String currency = str(d.get("currency"));
                BigDecimal amount = toBigDecimal(d.get("amount"));
                String payerDfsp = str(d.get("payerDfsp"));
                String payeeDfsp = str(d.get("payeeDfsp"));
                String windowId = str(d.get("windowId"));
                String settlementBatch = str(d.get("settlementBatch"));

                Object submitted = d.get("submittedOnDate");
                String submittedOnDate =
                        (submitted instanceof Date) ? isoFormatter.format(((Date) submitted).toInstant()) : str(
                                submitted);

                TransferData row = mapToTransferData(transferId,
                                                     state,
                                                     type,
                                                     currency,
                                                     amount,
                                                     payerDfsp,
                                                     payeeDfsp,
                                                     windowId,
                                                     settlementBatch,
                                                     submittedOnDate);
                list.add(row);
            }

            return new Output(list);

        } catch (Exception e) {
            throw new HubServicesException(HubServicesErrors.HUB_TRANSFER_ERROR.defaultMessage(e.getMessage()));
        }
    }

    // ----------------- Helpers -----------------

    private static Criteria and(Criteria base, Criteria extra) {

        return new Criteria().andOperator(base, extra);
    }

    private static ZoneId parseZoneOrDefault(String tz, ZoneId def) {

        try {
            if (StringUtils.isBlank(tz)) {return def;}
            return ZoneId.of(tz);
        } catch (Exception e) {
            return def;
        }
    }

    private static Instant parseInstant(String text, ZoneId zone) {

        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("Date string must not be blank");
        }
        // Accept full ISO or date-only (yyyy-MM-dd)
        try {
            return Instant.parse(text);
        } catch (Exception ignore) {
        }
        try {
            LocalDate ld = LocalDate.parse(text);
            return ZonedDateTime.of(ld.atStartOfDay(), zone).toInstant();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unparseable date: " + text, e);
        }
    }

    private static Instant parseInstantInclusiveEnd(String text, ZoneId zone) {
        // If it's ISO instant -> pass through. If it's date-only, go to end-of-day 23:59:59.999
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("Date string must not be blank");
        }
        try {
            return Instant.parse(text);
        } catch (Exception ignore) {
        }
        try {
            LocalDate ld = LocalDate.parse(text);
            return ZonedDateTime.of(ld.plusDays(1).atStartOfDay(), zone).toInstant().minusNanos(1_000_000); // ~.999
        } catch (Exception e) {
            throw new IllegalArgumentException("Unparseable date: " + text, e);
        }
    }

    private static String str(Object o) {

        return (o == null) ? "" : String.valueOf(o);
    }

    private static BigDecimal toBigDecimal(Object o) {

        if (o == null) {return null;}
        if (o instanceof BigDecimal bd) {return bd.setScale(2, RoundingMode.HALF_UP);}
        if (o instanceof Number n) {return BigDecimal.valueOf(n.doubleValue()).setScale(2, RoundingMode.HALF_UP);}
        try {
            return new BigDecimal(o.toString()).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * TODO: Replace this mapper with your actual TransferData construction.
     * This shows which fields are available from the aggregation.
     */
    private static TransferData mapToTransferData(String transferId,
                                                  String state,
                                                  String type,
                                                  String currency,
                                                  BigDecimal amount,
                                                  String payerDfsp,
                                                  String payeeDfsp,
                                                  String windowId,
                                                  String settlementBatch,
                                                  String submittedOnDate) {

        return new TransferData(transferId,
                                state,
                                type,
                                currency,
                                amount,
                                payerDfsp,
                                payeeDfsp,
                                windowId,
                                settlementBatch,
                                submittedOnDate);
    }

}