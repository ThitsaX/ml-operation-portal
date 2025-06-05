package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import java.util.*;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransactionInitiatorType is a Querydsl query type for CLTransactionInitiatorType
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransactionInitiatorType extends com.querydsl.sql.RelationalPathBase<CLTransactionInitiatorType> {

    private static final long serialVersionUID = 577204744;

    public static final CLTransactionInitiatorType transactionInitiatorType = new CLTransactionInitiatorType("transactionInitiatorType");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> transactionInitiatorTypeId = createNumber("transactionInitiatorTypeId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLTransactionInitiatorType> primary = createPrimaryKey(transactionInitiatorTypeId);

    public final com.querydsl.sql.ForeignKey<CLQuote> _quoteTransactioninitiatortypeidForeign = createInvForeignKey(Arrays.asList(transactionInitiatorTypeId, transactionInitiatorTypeId), Arrays.asList("transactionInitiatorTypeId", "transactionInitiatorTypeId"));

    public CLTransactionInitiatorType(String variable) {
        super(CLTransactionInitiatorType.class, forVariable(variable), "null", "transactionInitiatorType");
        addMetadata();
    }

    public CLTransactionInitiatorType(String variable, String schema, String table) {
        super(CLTransactionInitiatorType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransactionInitiatorType(String variable, String schema) {
        super(CLTransactionInitiatorType.class, forVariable(variable), schema, "transactionInitiatorType");
        addMetadata();
    }

    public CLTransactionInitiatorType(Path<? extends CLTransactionInitiatorType> path) {
        super(path.getType(), path.getMetadata(), "null", "transactionInitiatorType");
        addMetadata();
    }

    public CLTransactionInitiatorType(PathMetadata metadata) {
        super(CLTransactionInitiatorType.class, metadata, "null", "transactionInitiatorType");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(1024));
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(transactionInitiatorTypeId, ColumnMetadata.named("transactionInitiatorTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

