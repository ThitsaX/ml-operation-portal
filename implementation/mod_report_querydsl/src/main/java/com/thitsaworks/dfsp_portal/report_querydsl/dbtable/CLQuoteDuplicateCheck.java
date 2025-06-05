package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLQuoteDuplicateCheck is a Querydsl query type for CLQuoteDuplicateCheck
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLQuoteDuplicateCheck extends com.querydsl.sql.RelationalPathBase<CLQuoteDuplicateCheck> {

    private static final long serialVersionUID = -971210756;

    public static final CLQuoteDuplicateCheck quoteDuplicateCheck = new CLQuoteDuplicateCheck("quoteDuplicateCheck");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath hash = createString("hash");

    public final StringPath quoteId = createString("quoteId");

    public final com.querydsl.sql.PrimaryKey<CLQuoteDuplicateCheck> primary = createPrimaryKey(quoteId);

    public final com.querydsl.sql.ForeignKey<CLTransactionReference> _transactionreferenceQuoteidForeign = createInvForeignKey(quoteId, "quoteId");

    public CLQuoteDuplicateCheck(String variable) {
        super(CLQuoteDuplicateCheck.class, forVariable(variable), "null", "quoteDuplicateCheck");
        addMetadata();
    }

    public CLQuoteDuplicateCheck(String variable, String schema, String table) {
        super(CLQuoteDuplicateCheck.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLQuoteDuplicateCheck(String variable, String schema) {
        super(CLQuoteDuplicateCheck.class, forVariable(variable), schema, "quoteDuplicateCheck");
        addMetadata();
    }

    public CLQuoteDuplicateCheck(Path<? extends CLQuoteDuplicateCheck> path) {
        super(path.getType(), path.getMetadata(), "null", "quoteDuplicateCheck");
        addMetadata();
    }

    public CLQuoteDuplicateCheck(PathMetadata metadata) {
        super(CLQuoteDuplicateCheck.class, metadata, "null", "quoteDuplicateCheck");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(hash, ColumnMetadata.named("hash").withIndex(2).ofType(Types.VARCHAR).withSize(1024));
        addMetadata(quoteId, ColumnMetadata.named("quoteId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
    }

}

