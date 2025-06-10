package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLBulkTransferError is a Querydsl query type for CLBulkTransferError
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLBulkTransferError extends com.querydsl.sql.RelationalPathBase<CLBulkTransferError> {

    private static final long serialVersionUID = 989594958;

    public static final CLBulkTransferError bulkTransferError = new CLBulkTransferError("bulkTransferError");

    public final NumberPath<Long> bulkTransferErrorId = createNumber("bulkTransferErrorId", Long.class);

    public final NumberPath<Long> bulkTransferStateChangeId = createNumber("bulkTransferStateChangeId", Long.class);

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> errorCode = createNumber("errorCode", Integer.class);

    public final StringPath errorDescription = createString("errorDescription");

    public final com.querydsl.sql.PrimaryKey<CLBulkTransferError> primary = createPrimaryKey(bulkTransferErrorId);

    public final com.querydsl.sql.ForeignKey<CLBulkTransferStateChange> bulktransfererrorBulktransferstatechangeidForeign = createForeignKey(bulkTransferStateChangeId, "bulkTransferStateChangeId");

    public CLBulkTransferError(String variable) {
        super(CLBulkTransferError.class, forVariable(variable), "null", "bulkTransferError");
        addMetadata();
    }

    public CLBulkTransferError(String variable, String schema, String table) {
        super(CLBulkTransferError.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLBulkTransferError(String variable, String schema) {
        super(CLBulkTransferError.class, forVariable(variable), schema, "bulkTransferError");
        addMetadata();
    }

    public CLBulkTransferError(Path<? extends CLBulkTransferError> path) {
        super(path.getType(), path.getMetadata(), "null", "bulkTransferError");
        addMetadata();
    }

    public CLBulkTransferError(PathMetadata metadata) {
        super(CLBulkTransferError.class, metadata, "null", "bulkTransferError");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bulkTransferErrorId, ColumnMetadata.named("bulkTransferErrorId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(bulkTransferStateChangeId, ColumnMetadata.named("bulkTransferStateChangeId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(errorCode, ColumnMetadata.named("errorCode").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(errorDescription, ColumnMetadata.named("errorDescription").withIndex(4).ofType(Types.VARCHAR).withSize(128).notNull());
    }

}

