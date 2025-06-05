package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLExpiringTransfer is a Querydsl query type for CLExpiringTransfer
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLExpiringTransfer extends com.querydsl.sql.RelationalPathBase<CLExpiringTransfer> {

    private static final long serialVersionUID = -1372782236;

    public static final CLExpiringTransfer expiringTransfer = new CLExpiringTransfer("expiringTransfer");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> expirationDate = createDateTime("expirationDate", java.sql.Timestamp.class);

    public final NumberPath<Long> expiringTransferId = createNumber("expiringTransferId", Long.class);

    public final StringPath transferId = createString("transferId");

    public final com.querydsl.sql.PrimaryKey<CLExpiringTransfer> primary = createPrimaryKey(expiringTransferId);

    public final com.querydsl.sql.ForeignKey<CLTransfer> expiringtransferTransferidForeign = createForeignKey(transferId, "transferId");

    public CLExpiringTransfer(String variable) {
        super(CLExpiringTransfer.class, forVariable(variable), "null", "expiringTransfer");
        addMetadata();
    }

    public CLExpiringTransfer(String variable, String schema, String table) {
        super(CLExpiringTransfer.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLExpiringTransfer(String variable, String schema) {
        super(CLExpiringTransfer.class, forVariable(variable), schema, "expiringTransfer");
        addMetadata();
    }

    public CLExpiringTransfer(Path<? extends CLExpiringTransfer> path) {
        super(path.getType(), path.getMetadata(), "null", "expiringTransfer");
        addMetadata();
    }

    public CLExpiringTransfer(PathMetadata metadata) {
        super(CLExpiringTransfer.class, metadata, "null", "expiringTransfer");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(expirationDate, ColumnMetadata.named("expirationDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(expiringTransferId, ColumnMetadata.named("expiringTransferId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(transferId, ColumnMetadata.named("transferId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
    }

}

