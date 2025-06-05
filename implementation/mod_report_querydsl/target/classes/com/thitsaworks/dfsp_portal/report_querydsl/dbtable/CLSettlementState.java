package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlementState is a Querydsl query type for CLSettlementState
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementState extends com.querydsl.sql.RelationalPathBase<CLSettlementState> {

    private static final long serialVersionUID = -917615093;

    public static final CLSettlementState settlementState = new CLSettlementState("settlementState");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final StringPath enumeration = createString("enumeration");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath settlementStateId = createString("settlementStateId");

    public final com.querydsl.sql.PrimaryKey<CLSettlementState> primary = createPrimaryKey(settlementStateId);

    public final com.querydsl.sql.ForeignKey<CLSettlementStateChange> _settlementstatechangeSettlementstateidForeign = createInvForeignKey(settlementStateId, "settlementStateId");

    public final com.querydsl.sql.ForeignKey<CLSettlementParticipantCurrencyStateChange> _spcscSettlementstateidForeign = createInvForeignKey(settlementStateId, "settlementStateId");

    public CLSettlementState(String variable) {
        super(CLSettlementState.class, forVariable(variable), "null", "settlementState");
        addMetadata();
    }

    public CLSettlementState(String variable, String schema, String table) {
        super(CLSettlementState.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementState(String variable, String schema) {
        super(CLSettlementState.class, forVariable(variable), schema, "settlementState");
        addMetadata();
    }

    public CLSettlementState(Path<? extends CLSettlementState> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementState");
        addMetadata();
    }

    public CLSettlementState(PathMetadata metadata) {
        super(CLSettlementState.class, metadata, "null", "settlementState");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(enumeration, ColumnMetadata.named("enumeration").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(settlementStateId, ColumnMetadata.named("settlementStateId").withIndex(1).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

