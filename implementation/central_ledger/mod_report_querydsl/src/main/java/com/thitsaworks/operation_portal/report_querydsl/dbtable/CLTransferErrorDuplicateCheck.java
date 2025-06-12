package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransferErrorDuplicateCheck is a Querydsl query type for CLTransferErrorDuplicateCheck
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransferErrorDuplicateCheck extends com.querydsl.sql.RelationalPathBase<CLTransferErrorDuplicateCheck> {

    private static final long serialVersionUID = 454563229;

    public static final CLTransferErrorDuplicateCheck transferErrorDuplicateCheck = new CLTransferErrorDuplicateCheck("transferErrorDuplicateCheck");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath hash = createString("hash");

    public final StringPath transferId = createString("transferId");

    public final com.querydsl.sql.PrimaryKey<CLTransferErrorDuplicateCheck> primary = createPrimaryKey(transferId);

    public final com.querydsl.sql.ForeignKey<CLTransfer> transfererrorduplicatecheckTransferidForeign = createForeignKey(transferId, "transferId");

    public CLTransferErrorDuplicateCheck(String variable) {
        super(CLTransferErrorDuplicateCheck.class, forVariable(variable), "null", "transferErrorDuplicateCheck");
        addMetadata();
    }

    public CLTransferErrorDuplicateCheck(String variable, String schema, String table) {
        super(CLTransferErrorDuplicateCheck.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransferErrorDuplicateCheck(String variable, String schema) {
        super(CLTransferErrorDuplicateCheck.class, forVariable(variable), schema, "transferErrorDuplicateCheck");
        addMetadata();
    }

    public CLTransferErrorDuplicateCheck(Path<? extends CLTransferErrorDuplicateCheck> path) {
        super(path.getType(), path.getMetadata(), "null", "transferErrorDuplicateCheck");
        addMetadata();
    }

    public CLTransferErrorDuplicateCheck(PathMetadata metadata) {
        super(CLTransferErrorDuplicateCheck.class, metadata, "null", "transferErrorDuplicateCheck");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(hash, ColumnMetadata.named("hash").withIndex(2).ofType(Types.VARCHAR).withSize(256));
        addMetadata(transferId, ColumnMetadata.named("transferId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
    }

}

