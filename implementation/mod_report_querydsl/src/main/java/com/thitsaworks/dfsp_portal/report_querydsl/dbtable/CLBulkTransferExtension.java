package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLBulkTransferExtension is a Querydsl query type for CLBulkTransferExtension
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLBulkTransferExtension extends com.querydsl.sql.RelationalPathBase<CLBulkTransferExtension> {

    private static final long serialVersionUID = 1874672773;

    public static final CLBulkTransferExtension bulkTransferExtension = new CLBulkTransferExtension("bulkTransferExtension");

    public final NumberPath<Long> bulkTransferExtensionId = createNumber("bulkTransferExtensionId", Long.class);

    public final StringPath bulkTransferId = createString("bulkTransferId");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final BooleanPath isFulfilment = createBoolean("isFulfilment");

    public final StringPath key = createString("key");

    public final StringPath value = createString("value");

    public final com.querydsl.sql.PrimaryKey<CLBulkTransferExtension> primary = createPrimaryKey(bulkTransferExtensionId);

    public final com.querydsl.sql.ForeignKey<CLBulkTransfer> bulktransferextensionBulktransferidForeign = createForeignKey(bulkTransferId, "bulkTransferId");

    public CLBulkTransferExtension(String variable) {
        super(CLBulkTransferExtension.class, forVariable(variable), "null", "bulkTransferExtension");
        addMetadata();
    }

    public CLBulkTransferExtension(String variable, String schema, String table) {
        super(CLBulkTransferExtension.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLBulkTransferExtension(String variable, String schema) {
        super(CLBulkTransferExtension.class, forVariable(variable), schema, "bulkTransferExtension");
        addMetadata();
    }

    public CLBulkTransferExtension(Path<? extends CLBulkTransferExtension> path) {
        super(path.getType(), path.getMetadata(), "null", "bulkTransferExtension");
        addMetadata();
    }

    public CLBulkTransferExtension(PathMetadata metadata) {
        super(CLBulkTransferExtension.class, metadata, "null", "bulkTransferExtension");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bulkTransferExtensionId, ColumnMetadata.named("bulkTransferExtensionId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(bulkTransferId, ColumnMetadata.named("bulkTransferId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(6).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(isFulfilment, ColumnMetadata.named("isFulfilment").withIndex(3).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(key, ColumnMetadata.named("key").withIndex(4).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(5).ofType(Types.LONGVARCHAR).withSize(65535).notNull());
    }

}

