package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransactionReference is a Querydsl query type for CLTransactionReference
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransactionReference extends com.querydsl.sql.RelationalPathBase<CLTransactionReference> {

    private static final long serialVersionUID = -1839172918;

    public static final CLTransactionReference transactionReference = new CLTransactionReference("transactionReference");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath quoteId = createString("quoteId");

    public final StringPath transactionReferenceId = createString("transactionReferenceId");

    public final com.querydsl.sql.PrimaryKey<CLTransactionReference> primary = createPrimaryKey(transactionReferenceId);

    public final com.querydsl.sql.ForeignKey<CLQuoteDuplicateCheck> transactionreferenceQuoteidForeign = createForeignKey(quoteId, "quoteId");

    public final com.querydsl.sql.ForeignKey<CLQuote> _quoteTransactionreferenceidForeign = createInvForeignKey(transactionReferenceId, "transactionReferenceId");

    public final com.querydsl.sql.ForeignKey<CLQuoteExtension> _quoteextensionTransactionidForeign = createInvForeignKey(transactionReferenceId, "transactionId");

    public CLTransactionReference(String variable) {
        super(CLTransactionReference.class, forVariable(variable), "null", "transactionReference");
        addMetadata();
    }

    public CLTransactionReference(String variable, String schema, String table) {
        super(CLTransactionReference.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransactionReference(String variable, String schema) {
        super(CLTransactionReference.class, forVariable(variable), schema, "transactionReference");
        addMetadata();
    }

    public CLTransactionReference(Path<? extends CLTransactionReference> path) {
        super(path.getType(), path.getMetadata(), "null", "transactionReference");
        addMetadata();
    }

    public CLTransactionReference(PathMetadata metadata) {
        super(CLTransactionReference.class, metadata, "null", "transactionReference");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(quoteId, ColumnMetadata.named("quoteId").withIndex(2).ofType(Types.VARCHAR).withSize(36));
        addMetadata(transactionReferenceId, ColumnMetadata.named("transactionReferenceId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
    }

}

