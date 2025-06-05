package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlementStateChange is a Querydsl query type for CLSettlementStateChange
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementStateChange extends com.querydsl.sql.RelationalPathBase<CLSettlementStateChange> {

    private static final long serialVersionUID = -84125285;

    public static final CLSettlementStateChange settlementStateChange = new CLSettlementStateChange("settlementStateChange");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath reason = createString("reason");

    public final NumberPath<Long> settlementId = createNumber("settlementId", Long.class);

    public final NumberPath<Long> settlementStateChangeId = createNumber("settlementStateChangeId", Long.class);

    public final StringPath settlementStateId = createString("settlementStateId");

    public final com.querydsl.sql.PrimaryKey<CLSettlementStateChange> primary = createPrimaryKey(settlementStateChangeId);

    public final com.querydsl.sql.ForeignKey<CLSettlement> settlementstatechangeSettlementidForeign = createForeignKey(settlementId, "settlementId");

    public final com.querydsl.sql.ForeignKey<CLSettlementState> settlementstatechangeSettlementstateidForeign = createForeignKey(settlementStateId, "settlementStateId");

    public final com.querydsl.sql.ForeignKey<CLSettlement> _settlementCurrentstatechangeidForeign = createInvForeignKey(settlementStateChangeId, "currentStateChangeId");

    public CLSettlementStateChange(String variable) {
        super(CLSettlementStateChange.class, forVariable(variable), "null", "settlementStateChange");
        addMetadata();
    }

    public CLSettlementStateChange(String variable, String schema, String table) {
        super(CLSettlementStateChange.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementStateChange(String variable, String schema) {
        super(CLSettlementStateChange.class, forVariable(variable), schema, "settlementStateChange");
        addMetadata();
    }

    public CLSettlementStateChange(Path<? extends CLSettlementStateChange> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementStateChange");
        addMetadata();
    }

    public CLSettlementStateChange(PathMetadata metadata) {
        super(CLSettlementStateChange.class, metadata, "null", "settlementStateChange");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(reason, ColumnMetadata.named("reason").withIndex(4).ofType(Types.VARCHAR).withSize(512));
        addMetadata(settlementId, ColumnMetadata.named("settlementId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementStateChangeId, ColumnMetadata.named("settlementStateChangeId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementStateId, ColumnMetadata.named("settlementStateId").withIndex(3).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

