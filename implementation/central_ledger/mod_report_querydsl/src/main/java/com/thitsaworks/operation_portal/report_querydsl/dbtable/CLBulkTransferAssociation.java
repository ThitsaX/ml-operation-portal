package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLBulkTransferAssociation is a Querydsl query type for CLBulkTransferAssociation
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLBulkTransferAssociation extends com.querydsl.sql.RelationalPathBase<CLBulkTransferAssociation> {

    private static final long serialVersionUID = 2139226951;

    public static final CLBulkTransferAssociation bulkTransferAssociation = new CLBulkTransferAssociation("bulkTransferAssociation");

    public final NumberPath<Integer> bulkProcessingStateId = createNumber("bulkProcessingStateId", Integer.class);

    public final NumberPath<Long> bulkTransferAssociationId = createNumber("bulkTransferAssociationId", Long.class);

    public final StringPath bulkTransferId = createString("bulkTransferId");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> errorCode = createNumber("errorCode", Integer.class);

    public final StringPath errorDescription = createString("errorDescription");

    public final DateTimePath<java.sql.Timestamp> lastProcessedDate = createDateTime("lastProcessedDate", java.sql.Timestamp.class);

    public final StringPath transferId = createString("transferId");

    public final com.querydsl.sql.PrimaryKey<CLBulkTransferAssociation> primary = createPrimaryKey(bulkTransferAssociationId);

    public final com.querydsl.sql.ForeignKey<CLBulkProcessingState> bulktransferassociationBulkprocessingstateidForeign = createForeignKey(bulkProcessingStateId, "bulkProcessingStateId");

    public final com.querydsl.sql.ForeignKey<CLBulkTransfer> bulktransferassociationBulktransferidForeign = createForeignKey(bulkTransferId, "bulkTransferId");

    public CLBulkTransferAssociation(String variable) {
        super(CLBulkTransferAssociation.class, forVariable(variable), "null", "bulkTransferAssociation");
        addMetadata();
    }

    public CLBulkTransferAssociation(String variable, String schema, String table) {
        super(CLBulkTransferAssociation.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLBulkTransferAssociation(String variable, String schema) {
        super(CLBulkTransferAssociation.class, forVariable(variable), schema, "bulkTransferAssociation");
        addMetadata();
    }

    public CLBulkTransferAssociation(Path<? extends CLBulkTransferAssociation> path) {
        super(path.getType(), path.getMetadata(), "null", "bulkTransferAssociation");
        addMetadata();
    }

    public CLBulkTransferAssociation(PathMetadata metadata) {
        super(CLBulkTransferAssociation.class, metadata, "null", "bulkTransferAssociation");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bulkProcessingStateId, ColumnMetadata.named("bulkProcessingStateId").withIndex(5).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(bulkTransferAssociationId, ColumnMetadata.named("bulkTransferAssociationId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(bulkTransferId, ColumnMetadata.named("bulkTransferId").withIndex(3).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(errorCode, ColumnMetadata.named("errorCode").withIndex(7).ofType(Types.INTEGER).withSize(10));
        addMetadata(errorDescription, ColumnMetadata.named("errorDescription").withIndex(8).ofType(Types.VARCHAR).withSize(128));
        addMetadata(lastProcessedDate, ColumnMetadata.named("lastProcessedDate").withIndex(6).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(transferId, ColumnMetadata.named("transferId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
    }

}

