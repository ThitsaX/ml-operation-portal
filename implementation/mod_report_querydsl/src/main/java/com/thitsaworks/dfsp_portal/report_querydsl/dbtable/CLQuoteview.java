package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLQuoteview is a Querydsl query type for CLQuoteview
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLQuoteview extends com.querydsl.sql.RelationalPathBase<CLQuoteview> {

    private static final long serialVersionUID = -1626611068;

    public static final CLQuoteview quoteview = new CLQuoteview("quoteview");

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final StringPath amountType = createString("amountType");

    public final NumberPath<Integer> balanceOfPaymentsId = createNumber("balanceOfPaymentsId", Integer.class);

    public final StringPath currency = createString("currency");

    public final DateTimePath<java.sql.Timestamp> expirationDate = createDateTime("expirationDate", java.sql.Timestamp.class);

    public final StringPath note = createString("note");

    public final StringPath quoteId = createString("quoteId");

    public final StringPath transactionInitiator = createString("transactionInitiator");

    public final StringPath transactionInitiatorType = createString("transactionInitiatorType");

    public final StringPath transactionReferenceId = createString("transactionReferenceId");

    public final StringPath transactionRequestId = createString("transactionRequestId");

    public final StringPath transactionScenario = createString("transactionScenario");

    public final StringPath transactionSubScenario = createString("transactionSubScenario");

    public CLQuoteview(String variable) {
        super(CLQuoteview.class, forVariable(variable), "null", "quoteview");
        addMetadata();
    }

    public CLQuoteview(String variable, String schema, String table) {
        super(CLQuoteview.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLQuoteview(String variable, String schema) {
        super(CLQuoteview.class, forVariable(variable), schema, "quoteview");
        addMetadata();
    }

    public CLQuoteview(Path<? extends CLQuoteview> path) {
        super(path.getType(), path.getMetadata(), "null", "quoteview");
        addMetadata();
    }

    public CLQuoteview(PathMetadata metadata) {
        super(CLQuoteview.class, metadata, "null", "quoteview");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(amount, ColumnMetadata.named("amount").withIndex(12).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
        addMetadata(amountType, ColumnMetadata.named("amountType").withIndex(11).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(balanceOfPaymentsId, ColumnMetadata.named("balanceOfPaymentsId").withIndex(9).ofType(Types.INTEGER).withSize(10));
        addMetadata(currency, ColumnMetadata.named("currency").withIndex(13).ofType(Types.VARCHAR).withSize(255));
        addMetadata(expirationDate, ColumnMetadata.named("expirationDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(note, ColumnMetadata.named("note").withIndex(4).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(quoteId, ColumnMetadata.named("quoteId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(transactionInitiator, ColumnMetadata.named("transactionInitiator").withIndex(6).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(transactionInitiatorType, ColumnMetadata.named("transactionInitiatorType").withIndex(7).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(transactionReferenceId, ColumnMetadata.named("transactionReferenceId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(transactionRequestId, ColumnMetadata.named("transactionRequestId").withIndex(3).ofType(Types.VARCHAR).withSize(36));
        addMetadata(transactionScenario, ColumnMetadata.named("transactionScenario").withIndex(8).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(transactionSubScenario, ColumnMetadata.named("transactionSubScenario").withIndex(10).ofType(Types.VARCHAR).withSize(256));
    }

}

