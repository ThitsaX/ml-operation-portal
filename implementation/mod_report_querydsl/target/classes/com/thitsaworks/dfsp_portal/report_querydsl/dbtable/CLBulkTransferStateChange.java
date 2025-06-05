package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLBulkTransferStateChange is a Querydsl query type for CLBulkTransferStateChange
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLBulkTransferStateChange extends com.querydsl.sql.RelationalPathBase<CLBulkTransferStateChange> {

    private static final long serialVersionUID = -1346078105;

    public static final CLBulkTransferStateChange bulkTransferStateChange = new CLBulkTransferStateChange("bulkTransferStateChange");

    public final StringPath bulkTransferId = createString("bulkTransferId");

    public final NumberPath<Long> bulkTransferStateChangeId = createNumber("bulkTransferStateChangeId", Long.class);

    public final StringPath bulkTransferStateId = createString("bulkTransferStateId");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath reason = createString("reason");

    public final com.querydsl.sql.PrimaryKey<CLBulkTransferStateChange> primary = createPrimaryKey(bulkTransferStateChangeId);

    public final com.querydsl.sql.ForeignKey<CLBulkTransfer> bulktransferstatechangeBulktransferidForeign = createForeignKey(bulkTransferId, "bulkTransferId");

    public final com.querydsl.sql.ForeignKey<CLBulkTransferState> bulktransferstatechangeBulktransferstateidForeign = createForeignKey(bulkTransferStateId, "bulkTransferStateId");

    public final com.querydsl.sql.ForeignKey<CLBulkTransferError> _bulktransfererrorBulktransferstatechangeidForeign = createInvForeignKey(bulkTransferStateChangeId, "bulkTransferStateChangeId");

    public CLBulkTransferStateChange(String variable) {
        super(CLBulkTransferStateChange.class, forVariable(variable), "null", "bulkTransferStateChange");
        addMetadata();
    }

    public CLBulkTransferStateChange(String variable, String schema, String table) {
        super(CLBulkTransferStateChange.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLBulkTransferStateChange(String variable, String schema) {
        super(CLBulkTransferStateChange.class, forVariable(variable), schema, "bulkTransferStateChange");
        addMetadata();
    }

    public CLBulkTransferStateChange(Path<? extends CLBulkTransferStateChange> path) {
        super(path.getType(), path.getMetadata(), "null", "bulkTransferStateChange");
        addMetadata();
    }

    public CLBulkTransferStateChange(PathMetadata metadata) {
        super(CLBulkTransferStateChange.class, metadata, "null", "bulkTransferStateChange");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bulkTransferId, ColumnMetadata.named("bulkTransferId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(bulkTransferStateChangeId, ColumnMetadata.named("bulkTransferStateChangeId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(bulkTransferStateId, ColumnMetadata.named("bulkTransferStateId").withIndex(3).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(reason, ColumnMetadata.named("reason").withIndex(4).ofType(Types.VARCHAR).withSize(512));
    }

}

