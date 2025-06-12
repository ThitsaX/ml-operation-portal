package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlementWindow is a Querydsl query type for CLSettlementWindow
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementWindow extends com.querydsl.sql.RelationalPathBase<CLSettlementWindow> {

    private static final long serialVersionUID = 1723433398;

    public static final CLSettlementWindow settlementWindow = new CLSettlementWindow("settlementWindow");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Long> currentStateChangeId = createNumber("currentStateChangeId", Long.class);

    public final StringPath reason = createString("reason");

    public final NumberPath<Long> settlementWindowId = createNumber("settlementWindowId", Long.class);

    public final com.querydsl.sql.PrimaryKey<CLSettlementWindow> primary = createPrimaryKey(settlementWindowId);

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowStateChange> settlementwindowCurrentstatechangeidForeign = createForeignKey(currentStateChangeId, "settlementWindowStateChangeId");

    public final com.querydsl.sql.ForeignKey<CLSettlementSettlementWindow> _settlementsettlementwindowSettlementwindowidForeign = createInvForeignKey(settlementWindowId, "settlementWindowId");

    public final com.querydsl.sql.ForeignKey<CLSettlementTransferParticipant> _settlementtransferparticipantSettlementwindowidForeign = createInvForeignKey(settlementWindowId, "settlementWindowId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowContent> _settlementwindowcontentSettlementwindowidForeign = createInvForeignKey(settlementWindowId, "settlementWindowId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowStateChange> _settlementwindowstatechangeSettlementwindowidForeign = createInvForeignKey(settlementWindowId, "settlementWindowId");

    public final com.querydsl.sql.ForeignKey<CLTransferFulfilment> _transferfulfilmentSettlementwindowidForeign = createInvForeignKey(settlementWindowId, "settlementWindowId");

    public CLSettlementWindow(String variable) {
        super(CLSettlementWindow.class, forVariable(variable), "null", "settlementWindow");
        addMetadata();
    }

    public CLSettlementWindow(String variable, String schema, String table) {
        super(CLSettlementWindow.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementWindow(String variable, String schema) {
        super(CLSettlementWindow.class, forVariable(variable), schema, "settlementWindow");
        addMetadata();
    }

    public CLSettlementWindow(Path<? extends CLSettlementWindow> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementWindow");
        addMetadata();
    }

    public CLSettlementWindow(PathMetadata metadata) {
        super(CLSettlementWindow.class, metadata, "null", "settlementWindow");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(currentStateChangeId, ColumnMetadata.named("currentStateChangeId").withIndex(4).ofType(Types.BIGINT).withSize(20));
        addMetadata(reason, ColumnMetadata.named("reason").withIndex(2).ofType(Types.VARCHAR).withSize(512));
        addMetadata(settlementWindowId, ColumnMetadata.named("settlementWindowId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
    }

}

