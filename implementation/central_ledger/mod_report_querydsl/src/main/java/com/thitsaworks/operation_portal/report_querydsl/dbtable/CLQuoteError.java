package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLQuoteError is a Querydsl query type for CLQuoteError
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLQuoteError extends com.querydsl.sql.RelationalPathBase<CLQuoteError> {

    private static final long serialVersionUID = 1069692393;

    public static final CLQuoteError quoteError = new CLQuoteError("quoteError");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> errorCode = createNumber("errorCode", Integer.class);

    public final StringPath errorDescription = createString("errorDescription");

    public final NumberPath<Long> quoteErrorId = createNumber("quoteErrorId", Long.class);

    public final StringPath quoteId = createString("quoteId");

    public final NumberPath<Long> quoteResponseId = createNumber("quoteResponseId", Long.class);

    public final com.querydsl.sql.PrimaryKey<CLQuoteError> primary = createPrimaryKey(quoteErrorId);

    public final com.querydsl.sql.ForeignKey<CLQuote> quoteerrorQuoteidForeign = createForeignKey(quoteId, "quoteId");

    public final com.querydsl.sql.ForeignKey<CLQuoteResponse> quoteerrorQuoteresponseidForeign = createForeignKey(quoteResponseId, "quoteResponseId");

    public CLQuoteError(String variable) {
        super(CLQuoteError.class, forVariable(variable), "null", "quoteError");
        addMetadata();
    }

    public CLQuoteError(String variable, String schema, String table) {
        super(CLQuoteError.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLQuoteError(String variable, String schema) {
        super(CLQuoteError.class, forVariable(variable), schema, "quoteError");
        addMetadata();
    }

    public CLQuoteError(Path<? extends CLQuoteError> path) {
        super(path.getType(), path.getMetadata(), "null", "quoteError");
        addMetadata();
    }

    public CLQuoteError(PathMetadata metadata) {
        super(CLQuoteError.class, metadata, "null", "quoteError");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(6).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(errorCode, ColumnMetadata.named("errorCode").withIndex(4).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(errorDescription, ColumnMetadata.named("errorDescription").withIndex(5).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(quoteErrorId, ColumnMetadata.named("quoteErrorId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(quoteId, ColumnMetadata.named("quoteId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(quoteResponseId, ColumnMetadata.named("quoteResponseId").withIndex(3).ofType(Types.BIGINT).withSize(20));
    }

}

