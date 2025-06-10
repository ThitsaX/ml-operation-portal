package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLBulkTransferFulfilment is a Querydsl query type for CLBulkTransferFulfilment
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLBulkTransferFulfilment extends com.querydsl.sql.RelationalPathBase<CLBulkTransferFulfilment> {

    private static final long serialVersionUID = -1403381436;

    public static final CLBulkTransferFulfilment bulkTransferFulfilment = new CLBulkTransferFulfilment("bulkTransferFulfilment");

    public final StringPath bulkTransferId = createString("bulkTransferId");

    public final DateTimePath<java.sql.Timestamp> completedDate = createDateTime("completedDate", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final com.querydsl.sql.PrimaryKey<CLBulkTransferFulfilment> primary = createPrimaryKey(bulkTransferId);

    public final com.querydsl.sql.ForeignKey<CLBulkTransferFulfilmentDuplicateCheck> bulktransferfulfilmentBulktransferidForeign = createForeignKey(bulkTransferId, "bulkTransferId");

    public CLBulkTransferFulfilment(String variable) {
        super(CLBulkTransferFulfilment.class, forVariable(variable), "null", "bulkTransferFulfilment");
        addMetadata();
    }

    public CLBulkTransferFulfilment(String variable, String schema, String table) {
        super(CLBulkTransferFulfilment.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLBulkTransferFulfilment(String variable, String schema) {
        super(CLBulkTransferFulfilment.class, forVariable(variable), schema, "bulkTransferFulfilment");
        addMetadata();
    }

    public CLBulkTransferFulfilment(Path<? extends CLBulkTransferFulfilment> path) {
        super(path.getType(), path.getMetadata(), "null", "bulkTransferFulfilment");
        addMetadata();
    }

    public CLBulkTransferFulfilment(PathMetadata metadata) {
        super(CLBulkTransferFulfilment.class, metadata, "null", "bulkTransferFulfilment");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bulkTransferId, ColumnMetadata.named("bulkTransferId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(completedDate, ColumnMetadata.named("completedDate").withIndex(2).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
    }

}

