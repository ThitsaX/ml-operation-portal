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
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    private static final DateTimeFormatter WITH_OFFSET =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx");

    private final MongoTemplate reportingMongoReadTemplate;

    @Autowired
    public GetTransfersMongoQueryHandler(
            @Qualifier(PersistenceQualifiers.Hub.MONGO_READ_TEMPLATE)
            MongoTemplate reportingMongoReadTemplate) {
        this.reportingMongoReadTemplate = reportingMongoReadTemplate;
    }

    @Override
    public Output execute(Input input) throws HubServicesException {
        try {
            final int page = normalizePage(input.getPage());
            final int pageSize = normalizePageSize(input.getPageSize());
            final long skipN = (long) (page - 1) * pageSize;

            final Criteria criteria = buildCriteria(input);

            final long total = countTransfers(criteria);

            if (total == 0L) {
                return new Output(emptyList(), 0L);
            }

            final List<Document> items = fetchTransfers(criteria, skipN, pageSize);

            final ZoneId zone = resolveZone(input.getTimeZone());
            final List<TransferData> list = mapDocuments(items, zone);

            return new Output(list, total);

        } catch (Exception e) {
            throw new HubServicesException(
                    HubServicesErrors.HUB_TRANSFER_ERROR.defaultMessage(e.getMessage()));
        }
    }

    private Criteria buildCriteria(Input input) {
        final List<Criteria> filters = new ArrayList<>();

        if (StringUtils.isNotBlank(input.getTransferId())) {
            filters.add(Criteria.where("transferId").is(input.getTransferId()));

            if (StringUtils.isNotBlank(input.getFspName())) {
                filters.add(new Criteria().orOperator(
                        Criteria.where("payerDFSP").is(input.getFspName()),
                        Criteria.where("payeeDFSP").is(input.getFspName())
                                                     ));
            }

            return new Criteria().andOperator(filters.toArray(new Criteria[0]));
        }

        final Instant start = Instant.parse(input.getFromDate());
        final Instant end = Instant.parse(input.getToDate());

        filters.add(Criteria.where("lastUpdated")
                            .gte(Date.from(start))
                            .lte(Date.from(end)));

        if (StringUtils.isNotBlank(input.getFspName())) {
            filters.add(new Criteria().orOperator(
                    Criteria.where("payerDFSP").is(input.getFspName()),
                    Criteria.where("payeeDFSP").is(input.getFspName())
                                                 ));
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

        return new Criteria().andOperator(filters.toArray(new Criteria[0]));
    }

    private long countTransfers(Criteria criteria) {
        final Query countQuery = new Query(criteria);
        return reportingMongoReadTemplate.count(countQuery, COLLECTION);
    }

    private List<Document> fetchTransfers(Criteria criteria, long skipN, int pageSize) {
        final MatchOperation match = match(criteria);
        final SortOperation sort = sort(Sort.Direction.DESC, "lastUpdated");

        final AggregationOperation lookupSettlement = buildSettlementLookup();

        final AddFieldsOperation addFirstSettlement = AddFieldsOperation.builder()
                                                                        .addFieldWithValue("settlementMatch",
                                                                                           new Document("$arrayElemAt", Arrays.asList("$settlementMatches", 0)))
                                                                        .build();

        final ProjectionOperation project = project()
                .and("transferId").as("transferId")
                .and(ConditionalOperators.IfNull.ifNull("transferStateEnum").then("")).as("state")
                .and(ConditionalOperators.IfNull.ifNull("transactionType").then("")).as("type")
                .and(ConditionalOperators.IfNull.ifNull("transferTerms.transferAmount.currency").then("")).as("currency")
                .and(ArithmeticOperators.Round.roundValueOf("$transferTerms.transferAmount.amount").place(2)).as("amount")
                .and(ConditionalOperators.IfNull.ifNull("payerDFSP").then("")).as("payerDfsp")
                .and(ConditionalOperators.IfNull.ifNull("payerDesc").then("")).as("payerDfspName")
                .and(ConditionalOperators.IfNull.ifNull("payeeDFSP").then("")).as("payeeDfsp")
                .and(ConditionalOperators.IfNull.ifNull("payeeDesc").then("")).as("payeeDfspName")
                .and(ConditionalOperators.IfNull.ifNull("transferSettlementWindowId").then("")).as("windowId")
                .and(ConditionalOperators.IfNull.ifNull("settlementMatch.settlementId").then("")).as("settlementBatch")
                .and("lastUpdated").as("submittedOnDate");

        final Aggregation aggregation = newAggregation(
                match,
                sort,
                skip(skipN),
                limit(pageSize),
                lookupSettlement,
                addFirstSettlement,
                project
                                                      );

        final AggregationResults<Document> results =
                reportingMongoReadTemplate.aggregate(aggregation, COLLECTION, Document.class);

        return results.getMappedResults();
    }

    private AggregationOperation buildSettlementLookup() {
        return context -> {
            Document stage = new Document("$lookup",
                                          new Document("from", "settlement")
                                                  .append("let", new Document("winId",
                                                                              new Document("$toString", "$transferSettlementWindowId")))
                                                  .append("pipeline", Arrays.asList(
                                                          new Document("$addFields",
                                                                       new Document("_winIds",
                                                                                    new Document("$map",
                                                                                                 new Document("input", "$settlementWindows")
                                                                                                         .append("as", "w")
                                                                                                         .append("in",
                                                                                                                 new Document("$toString",
                                                                                                                              "$$w.settlementWindowId"))))),
                                                          new Document("$match",
                                                                       new Document("$expr",
                                                                                    new Document("$in", Arrays.asList("$$winId", "$_winIds")))),
                                                          new Document("$project",
                                                                       new Document("_id", 0)
                                                                               .append("settlementId", 1))
                                                                                   ))
                                                  .append("as", "settlementMatches")
            );

            return context.getMappedObject(stage);
        };
    }

    private List<TransferData> mapDocuments(List<Document> items, ZoneId zone) {
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

            final String submittedOnDate =
                    (submitted instanceof Date dateVal)
                            ? WITH_OFFSET.format(dateVal.toInstant().atZone(zone))
                            : String.valueOf(submitted);

            list.add(new TransferData(
                    transferId,
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
                    submittedOnDate
            ));
        }

        return list;
    }

    private static ZoneId resolveZone(String timeZone) {
        if ("0000".equals(timeZone)) {
            return ZoneOffset.UTC;
        }
        return parseZoneFlexible(timeZone);
    }

    private static int normalizePage(Integer page) {
        if (page == null || page < 1) {
            return DEFAULT_PAGE;
        }
        return page;
    }

    private static int normalizePageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    private static String str(Object o) {
        return (o == null) ? "" : String.valueOf(o);
    }

    private static BigDecimal toBigDecimal(Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof BigDecimal bd) {
            return bd.setScale(2, RoundingMode.DOWN);
        }

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

    private static ZoneId parseZoneFlexible(String raw) {
        if (raw == null || raw.isBlank()) {
            return ZoneOffset.UTC;
        }

        String s = raw.trim();

        if (s.equalsIgnoreCase("Z") || s.equalsIgnoreCase("UTC") || s.equalsIgnoreCase("GMT")) {
            return ZoneOffset.UTC;
        }

        try {
            return ZoneId.of(s);
        } catch (Exception ignored) {
        }

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
            try {
                return ZoneOffset.of(s);
            } catch (DateTimeException e) {
                throw new IllegalArgumentException("Unrecognized timezone/offset: " + raw, e);
            }
        }
    }
}