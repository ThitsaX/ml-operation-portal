package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLBulkTransferDuplicateCheck is a Querydsl query type for CLBulkTransferDuplicateCheck
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLBulkTransferDuplicateCheck extends com.querydsl.sql.RelationalPathBase<CLBulkTransferDuplicateCheck> {

    private static final long serialVersionUID = 510997047;

    public static final CLBulkTransferDuplicateCheck bulkTransferDuplicateCheck = new CLBulkTransferDuplicateCheck("bulkTransferDuplicateCheck");

    public final StringPath bulkTransferId = createString("bulkTransferId");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath hash = createString("hash");

    public final com.querydsl.sql.PrimaryKey<CLBulkTransferDuplicateCheck> primary = createPrimaryKey(bulkTransferId);

    public final com.querydsl.sql.ForeignKey<CLBulkTransfer> _bulktransferBulktransferidForeign = createInvForeignKey(bulkTransferId, "bulkTransferId");

    public CLBulkTransferDuplicateCheck(String variable) {
        super(CLBulkTransferDuplicateCheck.class, forVariable(variable), "null", "bulkTransferDuplicateCheck");
        addMetadata();
    }

    public CLBulkTransferDuplicateCheck(String variable, String schema, String table) {
        super(CLBulkTransferDuplicateCheck.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLBulkTransferDuplicateCheck(String variable, String schema) {
        super(CLBulkTransferDuplicateCheck.class, forVariable(variable), schema, "bulkTransferDuplicateCheck");
        addMetadata();
    }

    public CLBulkTransferDuplicateCheck(Path<? extends CLBulkTransferDuplicateCheck> path) {
        super(path.getType(), path.getMetadata(), "null", "bulkTransferDuplicateCheck");
        addMetadata();
    }

    public CLBulkTransferDuplicateCheck(PathMetadata metadata) {
        super(CLBulkTransferDuplicateCheck.class, metadata, "null", "bulkTransferDuplicateCheck");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bulkTransferId, ColumnMetadata.named("bulkTransferId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(hash, ColumnMetadata.named("hash").withIndex(2).ofType(Types.VARCHAR).withSize(256).notNull());
    }

}

