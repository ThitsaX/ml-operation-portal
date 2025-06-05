package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransferDuplicateCheck is a Querydsl query type for CLTransferDuplicateCheck
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransferDuplicateCheck extends com.querydsl.sql.RelationalPathBase<CLTransferDuplicateCheck> {

    private static final long serialVersionUID = 464729349;

    public static final CLTransferDuplicateCheck transferDuplicateCheck = new CLTransferDuplicateCheck("transferDuplicateCheck");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath hash = createString("hash");

    public final StringPath transferId = createString("transferId");

    public final com.querydsl.sql.PrimaryKey<CLTransferDuplicateCheck> primary = createPrimaryKey(transferId);

    public final com.querydsl.sql.ForeignKey<CLTransfer> _transferTransferidForeign = createInvForeignKey(transferId, "transferId");

    public CLTransferDuplicateCheck(String variable) {
        super(CLTransferDuplicateCheck.class, forVariable(variable), "null", "transferDuplicateCheck");
        addMetadata();
    }

    public CLTransferDuplicateCheck(String variable, String schema, String table) {
        super(CLTransferDuplicateCheck.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransferDuplicateCheck(String variable, String schema) {
        super(CLTransferDuplicateCheck.class, forVariable(variable), schema, "transferDuplicateCheck");
        addMetadata();
    }

    public CLTransferDuplicateCheck(Path<? extends CLTransferDuplicateCheck> path) {
        super(path.getType(), path.getMetadata(), "null", "transferDuplicateCheck");
        addMetadata();
    }

    public CLTransferDuplicateCheck(PathMetadata metadata) {
        super(CLTransferDuplicateCheck.class, metadata, "null", "transferDuplicateCheck");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(hash, ColumnMetadata.named("hash").withIndex(2).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(transferId, ColumnMetadata.named("transferId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
    }

}

