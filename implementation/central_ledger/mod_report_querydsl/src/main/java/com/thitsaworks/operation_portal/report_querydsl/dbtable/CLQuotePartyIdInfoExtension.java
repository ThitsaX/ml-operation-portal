package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLQuotePartyIdInfoExtension is a Querydsl query type for CLQuotePartyIdInfoExtension
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLQuotePartyIdInfoExtension extends com.querydsl.sql.RelationalPathBase<CLQuotePartyIdInfoExtension> {

    private static final long serialVersionUID = 1282323471;

    public static final CLQuotePartyIdInfoExtension quotePartyIdInfoExtension = new CLQuotePartyIdInfoExtension("quotePartyIdInfoExtension");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath key = createString("key");

    public final NumberPath<Long> quotePartyId = createNumber("quotePartyId", Long.class);

    public final NumberPath<Long> quotePartyIdInfoExtensionId = createNumber("quotePartyIdInfoExtensionId", Long.class);

    public final StringPath value = createString("value");

    public final com.querydsl.sql.PrimaryKey<CLQuotePartyIdInfoExtension> primary = createPrimaryKey(quotePartyIdInfoExtensionId);

    public final com.querydsl.sql.ForeignKey<CLQuoteParty> quotepartyidinfoextensionQuotepartyidForeign = createForeignKey(quotePartyId, "quotePartyId");

    public CLQuotePartyIdInfoExtension(String variable) {
        super(CLQuotePartyIdInfoExtension.class, forVariable(variable), "null", "quotePartyIdInfoExtension");
        addMetadata();
    }

    public CLQuotePartyIdInfoExtension(String variable, String schema, String table) {
        super(CLQuotePartyIdInfoExtension.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLQuotePartyIdInfoExtension(String variable, String schema) {
        super(CLQuotePartyIdInfoExtension.class, forVariable(variable), schema, "quotePartyIdInfoExtension");
        addMetadata();
    }

    public CLQuotePartyIdInfoExtension(Path<? extends CLQuotePartyIdInfoExtension> path) {
        super(path.getType(), path.getMetadata(), "null", "quotePartyIdInfoExtension");
        addMetadata();
    }

    public CLQuotePartyIdInfoExtension(PathMetadata metadata) {
        super(CLQuotePartyIdInfoExtension.class, metadata, "null", "quotePartyIdInfoExtension");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(key, ColumnMetadata.named("key").withIndex(3).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(quotePartyId, ColumnMetadata.named("quotePartyId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(quotePartyIdInfoExtensionId, ColumnMetadata.named("quotePartyIdInfoExtensionId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(4).ofType(Types.LONGVARCHAR).withSize(16777215).notNull());
    }

}

