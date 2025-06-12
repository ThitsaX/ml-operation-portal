package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLBulkTransferFulfilmentDuplicateCheck is a Querydsl query type for CLBulkTransferFulfilmentDuplicateCheck
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLBulkTransferFulfilmentDuplicateCheck extends com.querydsl.sql.RelationalPathBase<CLBulkTransferFulfilmentDuplicateCheck> {

    private static final long serialVersionUID = 251098401;

    public static final CLBulkTransferFulfilmentDuplicateCheck bulkTransferFulfilmentDuplicateCheck = new CLBulkTransferFulfilmentDuplicateCheck("bulkTransferFulfilmentDuplicateCheck");

    public final StringPath bulkTransferId = createString("bulkTransferId");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath hash = createString("hash");

    public final com.querydsl.sql.PrimaryKey<CLBulkTransferFulfilmentDuplicateCheck> primary = createPrimaryKey(bulkTransferId);

    public final com.querydsl.sql.ForeignKey<CLBulkTransfer> bulktransferfulfilmentduplicatecheckBulktransferidForeign = createForeignKey(bulkTransferId, "bulkTransferId");

    public final com.querydsl.sql.ForeignKey<CLBulkTransferFulfilment> _bulktransferfulfilmentBulktransferidForeign = createInvForeignKey(bulkTransferId, "bulkTransferId");

    public CLBulkTransferFulfilmentDuplicateCheck(String variable) {
        super(CLBulkTransferFulfilmentDuplicateCheck.class, forVariable(variable), "null", "bulkTransferFulfilmentDuplicateCheck");
        addMetadata();
    }

    public CLBulkTransferFulfilmentDuplicateCheck(String variable, String schema, String table) {
        super(CLBulkTransferFulfilmentDuplicateCheck.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLBulkTransferFulfilmentDuplicateCheck(String variable, String schema) {
        super(CLBulkTransferFulfilmentDuplicateCheck.class, forVariable(variable), schema, "bulkTransferFulfilmentDuplicateCheck");
        addMetadata();
    }

    public CLBulkTransferFulfilmentDuplicateCheck(Path<? extends CLBulkTransferFulfilmentDuplicateCheck> path) {
        super(path.getType(), path.getMetadata(), "null", "bulkTransferFulfilmentDuplicateCheck");
        addMetadata();
    }

    public CLBulkTransferFulfilmentDuplicateCheck(PathMetadata metadata) {
        super(CLBulkTransferFulfilmentDuplicateCheck.class, metadata, "null", "bulkTransferFulfilmentDuplicateCheck");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bulkTransferId, ColumnMetadata.named("bulkTransferId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(hash, ColumnMetadata.named("hash").withIndex(2).ofType(Types.VARCHAR).withSize(256).notNull());
    }

}

