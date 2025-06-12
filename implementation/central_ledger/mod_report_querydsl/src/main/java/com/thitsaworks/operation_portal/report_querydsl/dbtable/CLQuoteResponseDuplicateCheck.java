package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLQuoteResponseDuplicateCheck is a Querydsl query type for CLQuoteResponseDuplicateCheck
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLQuoteResponseDuplicateCheck extends com.querydsl.sql.RelationalPathBase<CLQuoteResponseDuplicateCheck> {

    private static final long serialVersionUID = -549737571;

    public static final CLQuoteResponseDuplicateCheck quoteResponseDuplicateCheck = new CLQuoteResponseDuplicateCheck("quoteResponseDuplicateCheck");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath hash = createString("hash");

    public final StringPath quoteId = createString("quoteId");

    public final NumberPath<Long> quoteResponseId = createNumber("quoteResponseId", Long.class);

    public final com.querydsl.sql.PrimaryKey<CLQuoteResponseDuplicateCheck> primary = createPrimaryKey(quoteResponseId);

    public final com.querydsl.sql.ForeignKey<CLQuote> quoteresponseduplicatecheckQuoteidForeign = createForeignKey(quoteId, "quoteId");

    public final com.querydsl.sql.ForeignKey<CLQuoteResponse> quoteresponseduplicatecheckQuoteresponseidForeign = createForeignKey(quoteResponseId, "quoteResponseId");

    public CLQuoteResponseDuplicateCheck(String variable) {
        super(CLQuoteResponseDuplicateCheck.class, forVariable(variable), "null", "quoteResponseDuplicateCheck");
        addMetadata();
    }

    public CLQuoteResponseDuplicateCheck(String variable, String schema, String table) {
        super(CLQuoteResponseDuplicateCheck.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLQuoteResponseDuplicateCheck(String variable, String schema) {
        super(CLQuoteResponseDuplicateCheck.class, forVariable(variable), schema, "quoteResponseDuplicateCheck");
        addMetadata();
    }

    public CLQuoteResponseDuplicateCheck(Path<? extends CLQuoteResponseDuplicateCheck> path) {
        super(path.getType(), path.getMetadata(), "null", "quoteResponseDuplicateCheck");
        addMetadata();
    }

    public CLQuoteResponseDuplicateCheck(PathMetadata metadata) {
        super(CLQuoteResponseDuplicateCheck.class, metadata, "null", "quoteResponseDuplicateCheck");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(hash, ColumnMetadata.named("hash").withIndex(3).ofType(Types.VARCHAR).withSize(255));
        addMetadata(quoteId, ColumnMetadata.named("quoteId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(quoteResponseId, ColumnMetadata.named("quoteResponseId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
    }

}

