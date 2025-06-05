package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransferState is a Querydsl query type for CLTransferState
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransferState extends com.querydsl.sql.RelationalPathBase<CLTransferState> {

    private static final long serialVersionUID = 1313142089;

    public static final CLTransferState transferState = new CLTransferState("transferState");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final StringPath enumeration = createString("enumeration");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath transferStateId = createString("transferStateId");

    public final com.querydsl.sql.PrimaryKey<CLTransferState> primary = createPrimaryKey(transferStateId);

    public final com.querydsl.sql.ForeignKey<CLTransferStateChange> _transferstatechangeTransferstateidForeign = createInvForeignKey(transferStateId, "transferStateId");

    public CLTransferState(String variable) {
        super(CLTransferState.class, forVariable(variable), "null", "transferState");
        addMetadata();
    }

    public CLTransferState(String variable, String schema, String table) {
        super(CLTransferState.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransferState(String variable, String schema) {
        super(CLTransferState.class, forVariable(variable), schema, "transferState");
        addMetadata();
    }

    public CLTransferState(Path<? extends CLTransferState> path) {
        super(path.getType(), path.getMetadata(), "null", "transferState");
        addMetadata();
    }

    public CLTransferState(PathMetadata metadata) {
        super(CLTransferState.class, metadata, "null", "transferState");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(enumeration, ColumnMetadata.named("enumeration").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(transferStateId, ColumnMetadata.named("transferStateId").withIndex(1).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

