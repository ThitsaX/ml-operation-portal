package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlementWindowStateChange is a Querydsl query type for CLSettlementWindowStateChange
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementWindowStateChange extends com.querydsl.sql.RelationalPathBase<CLSettlementWindowStateChange> {

    private static final long serialVersionUID = -423859189;

    public static final CLSettlementWindowStateChange settlementWindowStateChange = new CLSettlementWindowStateChange("settlementWindowStateChange");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath reason = createString("reason");

    public final NumberPath<Long> settlementWindowId = createNumber("settlementWindowId", Long.class);

    public final NumberPath<Long> settlementWindowStateChangeId = createNumber("settlementWindowStateChangeId", Long.class);

    public final StringPath settlementWindowStateId = createString("settlementWindowStateId");

    public final com.querydsl.sql.PrimaryKey<CLSettlementWindowStateChange> primary = createPrimaryKey(settlementWindowStateChangeId);

    public final com.querydsl.sql.ForeignKey<CLSettlementWindow> settlementwindowstatechangeSettlementwindowidForeign = createForeignKey(settlementWindowId, "settlementWindowId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowState> settlementwindowstatechangeSettlementwindowstateidForeign = createForeignKey(settlementWindowStateId, "settlementWindowStateId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindow> _settlementwindowCurrentstatechangeidForeign = createInvForeignKey(settlementWindowStateChangeId, "currentStateChangeId");

    public CLSettlementWindowStateChange(String variable) {
        super(CLSettlementWindowStateChange.class, forVariable(variable), "null", "settlementWindowStateChange");
        addMetadata();
    }

    public CLSettlementWindowStateChange(String variable, String schema, String table) {
        super(CLSettlementWindowStateChange.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementWindowStateChange(String variable, String schema) {
        super(CLSettlementWindowStateChange.class, forVariable(variable), schema, "settlementWindowStateChange");
        addMetadata();
    }

    public CLSettlementWindowStateChange(Path<? extends CLSettlementWindowStateChange> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementWindowStateChange");
        addMetadata();
    }

    public CLSettlementWindowStateChange(PathMetadata metadata) {
        super(CLSettlementWindowStateChange.class, metadata, "null", "settlementWindowStateChange");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(reason, ColumnMetadata.named("reason").withIndex(4).ofType(Types.VARCHAR).withSize(512));
        addMetadata(settlementWindowId, ColumnMetadata.named("settlementWindowId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementWindowStateChangeId, ColumnMetadata.named("settlementWindowStateChangeId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementWindowStateId, ColumnMetadata.named("settlementWindowStateId").withIndex(3).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

