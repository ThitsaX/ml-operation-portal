package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransferTimeout is a Querydsl query type for CLTransferTimeout
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransferTimeout extends com.querydsl.sql.RelationalPathBase<CLTransferTimeout> {

    private static final long serialVersionUID = -207605735;

    public static final CLTransferTimeout transferTimeout = new CLTransferTimeout("transferTimeout");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> expirationDate = createDateTime("expirationDate", java.sql.Timestamp.class);

    public final StringPath transferId = createString("transferId");

    public final NumberPath<Long> transferTimeoutId = createNumber("transferTimeoutId", Long.class);

    public final com.querydsl.sql.PrimaryKey<CLTransferTimeout> primary = createPrimaryKey(transferTimeoutId);

    public final com.querydsl.sql.ForeignKey<CLTransfer> transfertimeoutTransferidForeign = createForeignKey(transferId, "transferId");

    public CLTransferTimeout(String variable) {
        super(CLTransferTimeout.class, forVariable(variable), "null", "transferTimeout");
        addMetadata();
    }

    public CLTransferTimeout(String variable, String schema, String table) {
        super(CLTransferTimeout.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransferTimeout(String variable, String schema) {
        super(CLTransferTimeout.class, forVariable(variable), schema, "transferTimeout");
        addMetadata();
    }

    public CLTransferTimeout(Path<? extends CLTransferTimeout> path) {
        super(path.getType(), path.getMetadata(), "null", "transferTimeout");
        addMetadata();
    }

    public CLTransferTimeout(PathMetadata metadata) {
        super(CLTransferTimeout.class, metadata, "null", "transferTimeout");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(expirationDate, ColumnMetadata.named("expirationDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(transferId, ColumnMetadata.named("transferId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(transferTimeoutId, ColumnMetadata.named("transferTimeoutId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
    }

}

