package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLQuoteExtension is a Querydsl query type for CLQuoteExtension
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLQuoteExtension extends com.querydsl.sql.RelationalPathBase<CLQuoteExtension> {

    private static final long serialVersionUID = 1316202400;

    public static final CLQuoteExtension quoteExtension = new CLQuoteExtension("quoteExtension");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath key = createString("key");

    public final NumberPath<Long> quoteExtensionId = createNumber("quoteExtensionId", Long.class);

    public final StringPath quoteId = createString("quoteId");

    public final NumberPath<Long> quoteResponseId = createNumber("quoteResponseId", Long.class);

    public final StringPath transactionId = createString("transactionId");

    public final StringPath value = createString("value");

    public final com.querydsl.sql.PrimaryKey<CLQuoteExtension> primary = createPrimaryKey(quoteExtensionId);

    public final com.querydsl.sql.ForeignKey<CLQuote> quoteextensionQuoteidForeign = createForeignKey(quoteId, "quoteId");

    public final com.querydsl.sql.ForeignKey<CLQuoteResponse> quoteextensionQuoteresponseidForeign = createForeignKey(quoteResponseId, "quoteResponseId");

    public final com.querydsl.sql.ForeignKey<CLTransactionReference> quoteextensionTransactionidForeign = createForeignKey(transactionId, "transactionReferenceId");

    public CLQuoteExtension(String variable) {
        super(CLQuoteExtension.class, forVariable(variable), "null", "quoteExtension");
        addMetadata();
    }

    public CLQuoteExtension(String variable, String schema, String table) {
        super(CLQuoteExtension.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLQuoteExtension(String variable, String schema) {
        super(CLQuoteExtension.class, forVariable(variable), schema, "quoteExtension");
        addMetadata();
    }

    public CLQuoteExtension(Path<? extends CLQuoteExtension> path) {
        super(path.getType(), path.getMetadata(), "null", "quoteExtension");
        addMetadata();
    }

    public CLQuoteExtension(PathMetadata metadata) {
        super(CLQuoteExtension.class, metadata, "null", "quoteExtension");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(7).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(key, ColumnMetadata.named("key").withIndex(5).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(quoteExtensionId, ColumnMetadata.named("quoteExtensionId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(quoteId, ColumnMetadata.named("quoteId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(quoteResponseId, ColumnMetadata.named("quoteResponseId").withIndex(3).ofType(Types.BIGINT).withSize(20));
        addMetadata(transactionId, ColumnMetadata.named("transactionId").withIndex(4).ofType(Types.VARCHAR).withSize(36));
        addMetadata(value, ColumnMetadata.named("value").withIndex(6).ofType(Types.LONGVARCHAR).withSize(65535).notNull());
    }

}

