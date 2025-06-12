package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransferFulfilmentDuplicateCheck is a Querydsl query type for CLTransferFulfilmentDuplicateCheck
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransferFulfilmentDuplicateCheck extends com.querydsl.sql.RelationalPathBase<CLTransferFulfilmentDuplicateCheck> {

    private static final long serialVersionUID = -1339896209;

    public static final CLTransferFulfilmentDuplicateCheck transferFulfilmentDuplicateCheck = new CLTransferFulfilmentDuplicateCheck("transferFulfilmentDuplicateCheck");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath hash = createString("hash");

    public final StringPath transferId = createString("transferId");

    public final com.querydsl.sql.PrimaryKey<CLTransferFulfilmentDuplicateCheck> primary = createPrimaryKey(transferId);

    public final com.querydsl.sql.ForeignKey<CLTransfer> transferfulfilmentduplicatecheckTransferidForeign = createForeignKey(transferId, "transferId");

    public final com.querydsl.sql.ForeignKey<CLTransferFulfilment> _transferfulfilmentTransferidForeign = createInvForeignKey(transferId, "transferId");

    public CLTransferFulfilmentDuplicateCheck(String variable) {
        super(CLTransferFulfilmentDuplicateCheck.class, forVariable(variable), "null", "transferFulfilmentDuplicateCheck");
        addMetadata();
    }

    public CLTransferFulfilmentDuplicateCheck(String variable, String schema, String table) {
        super(CLTransferFulfilmentDuplicateCheck.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransferFulfilmentDuplicateCheck(String variable, String schema) {
        super(CLTransferFulfilmentDuplicateCheck.class, forVariable(variable), schema, "transferFulfilmentDuplicateCheck");
        addMetadata();
    }

    public CLTransferFulfilmentDuplicateCheck(Path<? extends CLTransferFulfilmentDuplicateCheck> path) {
        super(path.getType(), path.getMetadata(), "null", "transferFulfilmentDuplicateCheck");
        addMetadata();
    }

    public CLTransferFulfilmentDuplicateCheck(PathMetadata metadata) {
        super(CLTransferFulfilmentDuplicateCheck.class, metadata, "null", "transferFulfilmentDuplicateCheck");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(hash, ColumnMetadata.named("hash").withIndex(2).ofType(Types.VARCHAR).withSize(256));
        addMetadata(transferId, ColumnMetadata.named("transferId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
    }

}

