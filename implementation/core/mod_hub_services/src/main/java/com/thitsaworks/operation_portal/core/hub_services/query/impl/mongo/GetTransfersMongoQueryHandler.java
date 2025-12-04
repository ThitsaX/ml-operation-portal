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
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@Primary
public class GetTransfersMongoQueryHandler implements GetTransfersQuery {

    private static final String COLLECTION = "transaction";

    private static final int DEFAULT_PAGE = 1;

    private static final int DEFAULT_PAGE_SIZE = 25;

    private static final int MAX_PAGE_SIZE = 500;

    private final MongoTemplate reportingMongoReadTemplate;

    private static final DateTimeFormatter WITH_OFFSET = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    @Autowired
    public GetTransfersMongoQueryHandler(
            @Qualifier(PersistenceQualifiers.Hub.MONGO_READ_TEMPLATE) MongoTemplate reportingMongoReadTemplate) {

        this.reportingMongoReadTemplate = reportingMongoReadTemplate;
    }

    @Override
    public Output execute(Input input) throws HubServicesException {

        try {

            final int page = normalizePage(input.getPage());
            final int pageSize = normalizePageSize(input.getPageSize());
            final long skipN = (long) (page - 1) * pageSize;

            final AddFieldsOperation normalizeCreatedAt = AddFieldsOperation.builder()
                                                                            .addFieldWithValue("_createdAt",
                                                                                               ConvertOperators.ToDate.toDate(
                                                                                                       "$createdAt"))
                                                                            .build();

            final Instant start = Instant.parse(input.getFromDate());

            final Instant endInclusive = Instant.parse(input.getToDate());

            final Instant endEx = endInclusive.plusNanos(1);

            final Criteria baseDate = Criteria.where("_createdAt").gte(Date.from(start)).lt(Date.from(endEx));

            final List<Criteria> filters = new ArrayList<>();

            if (StringUtils.isNotBlank(input.getTransferId())) {

                filters.add(Criteria.where("transferId").is(input.getTransferId()));

                if (StringUtils.isNotBlank(input.getFspName())) {
                    filters.add(new Criteria().orOperator(Criteria.where("payerDFSP").is(input.getFspName()),
                                                          Criteria.where("payeeDFSP").is(input.getFspName())));
                }
            } else {

                filters.add(baseDate);

                if (StringUtils.isNotBlank(input.getFspName())) {
                    filters.add(new Criteria().orOperator(Criteria.where("payerDFSP").is(input.getFspName()),
                                                          Criteria.where("payeeDFSP").is(input.getFspName())));
                }
                if (StringUtils.isNotBlank(input.getPayerFspId())) {
                    filters.add(Criteria.where("payerDFSP").is(input.getPayerFspId()));
                }
                if (StringUtils.isNotBlank(input.getPayeeFspId())) {
                    filters.add(Criteria.where("payeeDFSP").is(input.getPayeeFspId()));
                }
                if (StringUtils.isNotBlank(input.getPayerIdentifierTypeId())) {
                    filters.add(Criteria.where("payerParty.partyIdType").is(input.getPayerIdentifierTypeId()));
                }
                if (StringUtils.isNotBlank(input.getPayerIdentifierValue())) {
                    filters.add(Criteria.where("payerParty.partyIdentifier").is(input.getPayerIdentifierValue()));
                }
                if (StringUtils.isNotBlank(input.getPayeeIdentifierTypeId())) {
                    filters.add(Criteria.where("payeeParty.partyIdType").is(input.getPayeeIdentifierTypeId()));
                }
                if (StringUtils.isNotBlank(input.getPayeeIdentifierValue())) {
                    filters.add(Criteria.where("payeeParty.partyIdentifier").is(input.getPayeeIdentifierValue()));
                }
                if (StringUtils.isNotBlank(input.getCurrencyId())) {
                    filters.add(Criteria.where("transferTerms.transferAmount.currency").is(input.getCurrencyId()));
                }
                if (StringUtils.isNotBlank(input.getTransferStateId())) {
                    filters.add(Criteria.where("transferStateEnum").is(input.getTransferStateId()));
                }
            }

            final MatchOperation match = match(new Criteria().andOperator(filters.toArray(new Criteria[0])));

            final AggregationOperation lookupSettlement = context -> {

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

            final AddFieldsOperation addFirstSettlement = AddFieldsOperation.builder().addFieldWithValue(
                    "settlementMatch",
                    new Document("$arrayElemAt", Arrays.asList("$settlementMatches", 0))).build();

            final ProjectionOperation project =
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
                             .and(ConditionalOperators.IfNull.ifNull("payerDesc").then(""))
                             .as("payerDfspName")
                             .and(ConditionalOperators.IfNull.ifNull("payeeDFSP").then(""))
                             .as("payeeDfsp")
                             .and(ConditionalOperators.IfNull.ifNull("payeeDesc").then(""))
                             .as("payeeDfspName")
                             .and(ConditionalOperators.IfNull.ifNull("transferSettlementWindowId").then(""))
                             .as("windowId")
                             .and(ConditionalOperators.IfNull.ifNull("settlementMatch.settlementId").then(""))
                             .as("settlementBatch")
                             .and("_createdAt")
                             .as("submittedOnDate");

            final SortOperation sort = sort(Sort.Direction.DESC, "submittedOnDate");

            final Aggregation agg = newAggregation(normalizeCreatedAt, match, facet(lookupSettlement,
                                                                                    addFirstSettlement,
                                                                                    project,
                                                                                    sort,
                                                                                    skip(skipN),
                                                                                    limit(pageSize)).as("items")
                                                                                                    .and(count().as(
                                                                                                            "total"))
                                                                                                    .as("meta"));

            final AggregationResults<Document> aggResults = reportingMongoReadTemplate.aggregate(agg,
                                                                                                 COLLECTION,
                                                                                                 Document.class);

            final Document rootDoc = aggResults.getUniqueMappedResult();
            if (rootDoc == null) {
                return new Output(emptyList(), 0);
            }

            @SuppressWarnings("unchecked") final List<Document> items = (List<Document>) rootDoc.getOrDefault("items",
                                                                                                              emptyList());

            @SuppressWarnings("unchecked") final List<Document> meta = (List<Document>) rootDoc.getOrDefault("meta",
                                                                                                             emptyList());

            final long total = meta.isEmpty() ? 0L : ((Number) meta.get(0).get("total")).longValue();

            final List<TransferData> list = new ArrayList<>(items.size());

            for (Document d : items) {
                final String transferId = str(d.get("transferId"));
                final String state = str(d.get("state"));
                final String type = str(d.get("type"));
                final String currency = str(d.get("currency"));
                final BigDecimal amount = toBigDecimal(d.get("amount"));
                final String payerDfsp = str(d.get("payerDfsp"));
                final String payerDfspName = str(d.get("payerDfspName"));
                final String payeeDfsp = str(d.get("payeeDfsp"));
                final String payeeDfspName = str(d.get("payeeDfspName"));
                final String windowId = str(d.get("windowId"));
                final String settlementBatch = str(d.get("settlementBatch"));

                final Object submitted = d.get("submittedOnDate");

                final ZoneId zone = parseZoneFlexible(input.getTimeZone());

                final String submittedOnDate =
                        (submitted instanceof Date dateVal)
                                ? WITH_OFFSET.format(dateVal.toInstant().atZone(zone))
                                : String.valueOf(submitted);

                list.add(mapToTransferData(transferId,
                                           state,
                                           type,
                                           currency,
                                           amount,
                                           payerDfsp,
                                           payerDfspName,
                                           payeeDfsp,
                                           payeeDfspName,
                                           windowId,
                                           settlementBatch,
                                           submittedOnDate));
            }

            return new Output(list, total);

        } catch (Exception e) {

            throw new HubServicesException(HubServicesErrors.HUB_TRANSFER_ERROR.defaultMessage(e.getMessage()));
        }
    }

    private static int normalizePage(Integer page) {

        if (page == null || page < 1) {return DEFAULT_PAGE;}
        return page;
    }

    private static int normalizePageSize(Integer pageSize) {

        if (pageSize == null || pageSize < 1) {return DEFAULT_PAGE_SIZE;}
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    private static String str(Object o) {

        return (o == null) ? "" : String.valueOf(o);
    }

    private static BigDecimal toBigDecimal(Object o) {

        if (o == null) {return null;}
        if (o instanceof BigDecimal bd) {return bd.setScale(2, RoundingMode.DOWN);}
        if (o instanceof Integer || o instanceof Long) {
            return BigDecimal.valueOf(((Number) o).longValue()).setScale(2, RoundingMode.DOWN);
        }
        if (o instanceof Number n) {

            return BigDecimal.valueOf(n.doubleValue()).setScale(2, RoundingMode.DOWN);
        }
        try {
            return new BigDecimal(o.toString()).setScale(2, RoundingMode.DOWN);
        } catch (Exception e) {
            return null;
        }
    }

    private static TransferData mapToTransferData(String transferId,
                                                  String state,
                                                  String type,
                                                  String currency,
                                                  BigDecimal amount,
                                                  String payerDfsp,
                                                  String payerDfspName,
                                                  String payeeDfsp,
                                                  String payeeDfspName,
                                                  String windowId,
                                                  String settlementBatch,
                                                  String submittedOnDate) {

        return new TransferData(transferId,
                                state,
                                type,
                                currency,
                                amount,
                                payerDfsp,
                                payerDfspName,
                                payeeDfsp,
                                payeeDfspName,
                                windowId,
                                settlementBatch,
                                submittedOnDate);
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
