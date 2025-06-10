package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransferExtension is a Querydsl query type for CLTransferExtension
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransferExtension extends com.querydsl.sql.RelationalPathBase<CLTransferExtension> {

    private static final long serialVersionUID = 1690317047;

    public static final CLTransferExtension transferExtension = new CLTransferExtension("transferExtension");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final BooleanPath isError = createBoolean("isError");

    public final BooleanPath isFulfilment = createBoolean("isFulfilment");

    public final StringPath key = createString("key");

    public final NumberPath<Long> transferExtensionId = createNumber("transferExtensionId", Long.class);

    public final StringPath transferId = createString("transferId");

    public final StringPath value = createString("value");

    public final com.querydsl.sql.PrimaryKey<CLTransferExtension> primary = createPrimaryKey(transferExtensionId);

    public final com.querydsl.sql.ForeignKey<CLTransfer> transferextensionTransferidForeign = createForeignKey(transferId, "transferId");

    public CLTransferExtension(String variable) {
        super(CLTransferExtension.class, forVariable(variable), "null", "transferExtension");
        addMetadata();
    }

    public CLTransferExtension(String variable, String schema, String table) {
        super(CLTransferExtension.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransferExtension(String variable, String schema) {
        super(CLTransferExtension.class, forVariable(variable), schema, "transferExtension");
        addMetadata();
    }

    public CLTransferExtension(Path<? extends CLTransferExtension> path) {
        super(path.getType(), path.getMetadata(), "null", "transferExtension");
        addMetadata();
    }

    public CLTransferExtension(PathMetadata metadata) {
        super(CLTransferExtension.class, metadata, "null", "transferExtension");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(7).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(isError, ColumnMetadata.named("isError").withIndex(6).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(isFulfilment, ColumnMetadata.named("isFulfilment").withIndex(5).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(key, ColumnMetadata.named("key").withIndex(3).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(transferExtensionId, ColumnMetadata.named("transferExtensionId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(transferId, ColumnMetadata.named("transferId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(4).ofType(Types.LONGVARCHAR).withSize(65535).notNull());
    }

}

