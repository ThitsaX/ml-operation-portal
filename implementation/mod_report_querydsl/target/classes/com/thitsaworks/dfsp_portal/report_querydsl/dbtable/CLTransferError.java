package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransferError is a Querydsl query type for CLTransferError
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransferError extends com.querydsl.sql.RelationalPathBase<CLTransferError> {

    private static final long serialVersionUID = 1300169408;

    public static final CLTransferError transferError = new CLTransferError("transferError");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> errorCode = createNumber("errorCode", Integer.class);

    public final StringPath errorDescription = createString("errorDescription");

    public final StringPath transferId = createString("transferId");

    public final NumberPath<Long> transferStateChangeId = createNumber("transferStateChangeId", Long.class);

    public final com.querydsl.sql.PrimaryKey<CLTransferError> primary = createPrimaryKey(transferId);

    public final com.querydsl.sql.ForeignKey<CLTransferStateChange> transfererrorTransferstatechangeidForeign = createForeignKey(transferStateChangeId, "transferStateChangeId");

    public CLTransferError(String variable) {
        super(CLTransferError.class, forVariable(variable), "null", "transferError");
        addMetadata();
    }

    public CLTransferError(String variable, String schema, String table) {
        super(CLTransferError.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransferError(String variable, String schema) {
        super(CLTransferError.class, forVariable(variable), schema, "transferError");
        addMetadata();
    }

    public CLTransferError(Path<? extends CLTransferError> path) {
        super(path.getType(), path.getMetadata(), "null", "transferError");
        addMetadata();
    }

    public CLTransferError(PathMetadata metadata) {
        super(CLTransferError.class, metadata, "null", "transferError");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(errorCode, ColumnMetadata.named("errorCode").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(errorDescription, ColumnMetadata.named("errorDescription").withIndex(4).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(transferId, ColumnMetadata.named("transferId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(transferStateChangeId, ColumnMetadata.named("transferStateChangeId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
    }

}

