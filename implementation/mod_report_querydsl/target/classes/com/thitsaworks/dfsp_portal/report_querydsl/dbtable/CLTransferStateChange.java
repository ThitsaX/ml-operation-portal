package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransferStateChange is a Querydsl query type for CLTransferStateChange
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransferStateChange extends com.querydsl.sql.RelationalPathBase<CLTransferStateChange> {

    private static final long serialVersionUID = 1876695641;

    public static final CLTransferStateChange transferStateChange = new CLTransferStateChange("transferStateChange");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath reason = createString("reason");

    public final StringPath transferId = createString("transferId");

    public final NumberPath<Long> transferStateChangeId = createNumber("transferStateChangeId", Long.class);

    public final StringPath transferStateId = createString("transferStateId");

    public final com.querydsl.sql.PrimaryKey<CLTransferStateChange> primary = createPrimaryKey(transferStateChangeId);

    public final com.querydsl.sql.ForeignKey<CLTransfer> transferstatechangeTransferidForeign = createForeignKey(transferId, "transferId");

    public final com.querydsl.sql.ForeignKey<CLTransferState> transferstatechangeTransferstateidForeign = createForeignKey(transferStateId, "transferStateId");

    public final com.querydsl.sql.ForeignKey<CLParticipantPositionChange> _participantpositionchangeTransferstatechangeidForeign = createInvForeignKey(transferStateChangeId, "transferStateChangeId");

    public final com.querydsl.sql.ForeignKey<CLTransferError> _transfererrorTransferstatechangeidForeign = createInvForeignKey(transferStateChangeId, "transferStateChangeId");

    public CLTransferStateChange(String variable) {
        super(CLTransferStateChange.class, forVariable(variable), "null", "transferStateChange");
        addMetadata();
    }

    public CLTransferStateChange(String variable, String schema, String table) {
        super(CLTransferStateChange.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransferStateChange(String variable, String schema) {
        super(CLTransferStateChange.class, forVariable(variable), schema, "transferStateChange");
        addMetadata();
    }

    public CLTransferStateChange(Path<? extends CLTransferStateChange> path) {
        super(path.getType(), path.getMetadata(), "null", "transferStateChange");
        addMetadata();
    }

    public CLTransferStateChange(PathMetadata metadata) {
        super(CLTransferStateChange.class, metadata, "null", "transferStateChange");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(reason, ColumnMetadata.named("reason").withIndex(4).ofType(Types.VARCHAR).withSize(512));
        addMetadata(transferId, ColumnMetadata.named("transferId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(transferStateChangeId, ColumnMetadata.named("transferStateChangeId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(transferStateId, ColumnMetadata.named("transferStateId").withIndex(3).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

