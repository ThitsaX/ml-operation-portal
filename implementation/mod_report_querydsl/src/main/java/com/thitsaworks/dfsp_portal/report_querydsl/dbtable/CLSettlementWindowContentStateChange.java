package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlementWindowContentStateChange is a Querydsl query type for CLSettlementWindowContentStateChange
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementWindowContentStateChange extends com.querydsl.sql.RelationalPathBase<CLSettlementWindowContentStateChange> {

    private static final long serialVersionUID = 1351413182;

    public static final CLSettlementWindowContentStateChange settlementWindowContentStateChange = new CLSettlementWindowContentStateChange("settlementWindowContentStateChange");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath reason = createString("reason");

    public final NumberPath<Long> settlementWindowContentId = createNumber("settlementWindowContentId", Long.class);

    public final NumberPath<Long> settlementWindowContentStateChangeId = createNumber("settlementWindowContentStateChangeId", Long.class);

    public final StringPath settlementWindowStateId = createString("settlementWindowStateId");

    public final com.querydsl.sql.PrimaryKey<CLSettlementWindowContentStateChange> primary = createPrimaryKey(settlementWindowContentStateChangeId);

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowContent> swcSettlementwindowcontentidForeign = createForeignKey(settlementWindowContentId, "settlementWindowContentId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowState> sws1SettlementwindowstateidForeign = createForeignKey(settlementWindowStateId, "settlementWindowStateId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowContent> _settlementwindowcontentCurrentstatechangeidForeign = createInvForeignKey(settlementWindowContentStateChangeId, "currentStateChangeId");

    public CLSettlementWindowContentStateChange(String variable) {
        super(CLSettlementWindowContentStateChange.class, forVariable(variable), "null", "settlementWindowContentStateChange");
        addMetadata();
    }

    public CLSettlementWindowContentStateChange(String variable, String schema, String table) {
        super(CLSettlementWindowContentStateChange.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementWindowContentStateChange(String variable, String schema) {
        super(CLSettlementWindowContentStateChange.class, forVariable(variable), schema, "settlementWindowContentStateChange");
        addMetadata();
    }

    public CLSettlementWindowContentStateChange(Path<? extends CLSettlementWindowContentStateChange> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementWindowContentStateChange");
        addMetadata();
    }

    public CLSettlementWindowContentStateChange(PathMetadata metadata) {
        super(CLSettlementWindowContentStateChange.class, metadata, "null", "settlementWindowContentStateChange");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(reason, ColumnMetadata.named("reason").withIndex(4).ofType(Types.VARCHAR).withSize(512));
        addMetadata(settlementWindowContentId, ColumnMetadata.named("settlementWindowContentId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementWindowContentStateChangeId, ColumnMetadata.named("settlementWindowContentStateChangeId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementWindowStateId, ColumnMetadata.named("settlementWindowStateId").withIndex(3).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

