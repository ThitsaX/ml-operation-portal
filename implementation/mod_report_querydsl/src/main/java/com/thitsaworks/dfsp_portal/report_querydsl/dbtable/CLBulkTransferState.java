package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLBulkTransferState is a Querydsl query type for CLBulkTransferState
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLBulkTransferState extends com.querydsl.sql.RelationalPathBase<CLBulkTransferState> {

    private static final long serialVersionUID = 1002567639;

    public static final CLBulkTransferState bulkTransferState = new CLBulkTransferState("bulkTransferState");

    public final StringPath bulkTransferStateId = createString("bulkTransferStateId");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final StringPath enumeration = createString("enumeration");

    public final BooleanPath isActive = createBoolean("isActive");

    public final com.querydsl.sql.PrimaryKey<CLBulkTransferState> primary = createPrimaryKey(bulkTransferStateId);

    public final com.querydsl.sql.ForeignKey<CLBulkTransferStateChange> _bulktransferstatechangeBulktransferstateidForeign = createInvForeignKey(bulkTransferStateId, "bulkTransferStateId");

    public CLBulkTransferState(String variable) {
        super(CLBulkTransferState.class, forVariable(variable), "null", "bulkTransferState");
        addMetadata();
    }

    public CLBulkTransferState(String variable, String schema, String table) {
        super(CLBulkTransferState.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLBulkTransferState(String variable, String schema) {
        super(CLBulkTransferState.class, forVariable(variable), schema, "bulkTransferState");
        addMetadata();
    }

    public CLBulkTransferState(Path<? extends CLBulkTransferState> path) {
        super(path.getType(), path.getMetadata(), "null", "bulkTransferState");
        addMetadata();
    }

    public CLBulkTransferState(PathMetadata metadata) {
        super(CLBulkTransferState.class, metadata, "null", "bulkTransferState");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bulkTransferStateId, ColumnMetadata.named("bulkTransferStateId").withIndex(1).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(enumeration, ColumnMetadata.named("enumeration").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
    }

}

