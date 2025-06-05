package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransferFulfilment is a Querydsl query type for CLTransferFulfilment
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransferFulfilment extends com.querydsl.sql.RelationalPathBase<CLTransferFulfilment> {

    private static final long serialVersionUID = 1471525650;

    public static final CLTransferFulfilment transferFulfilment = new CLTransferFulfilment("transferFulfilment");

    public final DateTimePath<java.sql.Timestamp> completedDate = createDateTime("completedDate", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath ilpFulfilment = createString("ilpFulfilment");

    public final BooleanPath isValid = createBoolean("isValid");

    public final NumberPath<Long> settlementWindowId = createNumber("settlementWindowId", Long.class);

    public final StringPath transferId = createString("transferId");

    public final com.querydsl.sql.PrimaryKey<CLTransferFulfilment> primary = createPrimaryKey(transferId);

    public final com.querydsl.sql.ForeignKey<CLSettlementWindow> transferfulfilmentSettlementwindowidForeign = createForeignKey(settlementWindowId, "settlementWindowId");

    public final com.querydsl.sql.ForeignKey<CLTransferFulfilmentDuplicateCheck> transferfulfilmentTransferidForeign = createForeignKey(transferId, "transferId");

    public CLTransferFulfilment(String variable) {
        super(CLTransferFulfilment.class, forVariable(variable), "null", "transferFulfilment");
        addMetadata();
    }

    public CLTransferFulfilment(String variable, String schema, String table) {
        super(CLTransferFulfilment.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransferFulfilment(String variable, String schema) {
        super(CLTransferFulfilment.class, forVariable(variable), schema, "transferFulfilment");
        addMetadata();
    }

    public CLTransferFulfilment(Path<? extends CLTransferFulfilment> path) {
        super(path.getType(), path.getMetadata(), "null", "transferFulfilment");
        addMetadata();
    }

    public CLTransferFulfilment(PathMetadata metadata) {
        super(CLTransferFulfilment.class, metadata, "null", "transferFulfilment");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(completedDate, ColumnMetadata.named("completedDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(6).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(ilpFulfilment, ColumnMetadata.named("ilpFulfilment").withIndex(2).ofType(Types.VARCHAR).withSize(256));
        addMetadata(isValid, ColumnMetadata.named("isValid").withIndex(4).ofType(Types.BIT).withSize(1));
        addMetadata(settlementWindowId, ColumnMetadata.named("settlementWindowId").withIndex(5).ofType(Types.BIGINT).withSize(20));
        addMetadata(transferId, ColumnMetadata.named("transferId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
    }

}

