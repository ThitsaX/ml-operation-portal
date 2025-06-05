package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlementWindowState is a Querydsl query type for CLSettlementWindowState
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementWindowState extends com.querydsl.sql.RelationalPathBase<CLSettlementWindowState> {

    private static final long serialVersionUID = 1097397371;

    public static final CLSettlementWindowState settlementWindowState = new CLSettlementWindowState("settlementWindowState");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final StringPath enumeration = createString("enumeration");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath settlementWindowStateId = createString("settlementWindowStateId");

    public final com.querydsl.sql.PrimaryKey<CLSettlementWindowState> primary = createPrimaryKey(settlementWindowStateId);

    public final com.querydsl.sql.ForeignKey<CLSettlementContentAggregation> _settlementcontentaggregationCurrentstateidForeign = createInvForeignKey(settlementWindowStateId, "currentStateId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowStateChange> _settlementwindowstatechangeSettlementwindowstateidForeign = createInvForeignKey(settlementWindowStateId, "settlementWindowStateId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowContentStateChange> _sws1SettlementwindowstateidForeign = createInvForeignKey(settlementWindowStateId, "settlementWindowStateId");

    public CLSettlementWindowState(String variable) {
        super(CLSettlementWindowState.class, forVariable(variable), "null", "settlementWindowState");
        addMetadata();
    }

    public CLSettlementWindowState(String variable, String schema, String table) {
        super(CLSettlementWindowState.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementWindowState(String variable, String schema) {
        super(CLSettlementWindowState.class, forVariable(variable), schema, "settlementWindowState");
        addMetadata();
    }

    public CLSettlementWindowState(Path<? extends CLSettlementWindowState> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementWindowState");
        addMetadata();
    }

    public CLSettlementWindowState(PathMetadata metadata) {
        super(CLSettlementWindowState.class, metadata, "null", "settlementWindowState");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(enumeration, ColumnMetadata.named("enumeration").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(settlementWindowStateId, ColumnMetadata.named("settlementWindowStateId").withIndex(1).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

