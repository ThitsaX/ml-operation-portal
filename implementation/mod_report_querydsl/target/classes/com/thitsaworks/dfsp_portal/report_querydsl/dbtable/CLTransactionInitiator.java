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
 * CLTransactionInitiator is a Querydsl query type for CLTransactionInitiator
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransactionInitiator extends com.querydsl.sql.RelationalPathBase<CLTransactionInitiator> {

    private static final long serialVersionUID = -1163004498;

    public static final CLTransactionInitiator transactionInitiator = new CLTransactionInitiator("transactionInitiator");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> transactionInitiatorId = createNumber("transactionInitiatorId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLTransactionInitiator> primary = createPrimaryKey(transactionInitiatorId);

    public final com.querydsl.sql.ForeignKey<CLQuote> _quoteTransactioninitiatoridForeign = createInvForeignKey(Arrays.asList(transactionInitiatorId, transactionInitiatorId), Arrays.asList("transactionInitiatorId", "transactionInitiatorId"));

    public CLTransactionInitiator(String variable) {
        super(CLTransactionInitiator.class, forVariable(variable), "null", "transactionInitiator");
        addMetadata();
    }

    public CLTransactionInitiator(String variable, String schema, String table) {
        super(CLTransactionInitiator.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransactionInitiator(String variable, String schema) {
        super(CLTransactionInitiator.class, forVariable(variable), schema, "transactionInitiator");
        addMetadata();
    }

    public CLTransactionInitiator(Path<? extends CLTransactionInitiator> path) {
        super(path.getType(), path.getMetadata(), "null", "transactionInitiator");
        addMetadata();
    }

    public CLTransactionInitiator(PathMetadata metadata) {
        super(CLTransactionInitiator.class, metadata, "null", "transactionInitiator");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(1024));
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(transactionInitiatorId, ColumnMetadata.named("transactionInitiatorId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

