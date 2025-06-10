package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlementParticipantCurrencyStateChange is a Querydsl query type for CLSettlementParticipantCurrencyStateChange
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementParticipantCurrencyStateChange extends com.querydsl.sql.RelationalPathBase<CLSettlementParticipantCurrencyStateChange> {

    private static final long serialVersionUID = 687892579;

    public static final CLSettlementParticipantCurrencyStateChange settlementParticipantCurrencyStateChange = new CLSettlementParticipantCurrencyStateChange("settlementParticipantCurrencyStateChange");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath externalReference = createString("externalReference");

    public final StringPath reason = createString("reason");

    public final NumberPath<Long> settlementParticipantCurrencyId = createNumber("settlementParticipantCurrencyId", Long.class);

    public final NumberPath<Long> settlementParticipantCurrencyStateChangeId = createNumber("settlementParticipantCurrencyStateChangeId", Long.class);

    public final StringPath settlementStateId = createString("settlementStateId");

    public final com.querydsl.sql.PrimaryKey<CLSettlementParticipantCurrencyStateChange> primary = createPrimaryKey(settlementParticipantCurrencyStateChangeId);

    public final com.querydsl.sql.ForeignKey<CLSettlementParticipantCurrency> spcscSettlementparticipantcurrencyidForeign = createForeignKey(settlementParticipantCurrencyId, "settlementParticipantCurrencyId");

    public final com.querydsl.sql.ForeignKey<CLSettlementState> spcscSettlementstateidForeign = createForeignKey(settlementStateId, "settlementStateId");

    public final com.querydsl.sql.ForeignKey<CLSettlementParticipantCurrency> _spcCurrentstatechangeidForeign = createInvForeignKey(settlementParticipantCurrencyStateChangeId, "currentStateChangeId");

    public CLSettlementParticipantCurrencyStateChange(String variable) {
        super(CLSettlementParticipantCurrencyStateChange.class, forVariable(variable), "null", "settlementParticipantCurrencyStateChange");
        addMetadata();
    }

    public CLSettlementParticipantCurrencyStateChange(String variable, String schema, String table) {
        super(CLSettlementParticipantCurrencyStateChange.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementParticipantCurrencyStateChange(String variable, String schema) {
        super(CLSettlementParticipantCurrencyStateChange.class, forVariable(variable), schema, "settlementParticipantCurrencyStateChange");
        addMetadata();
    }

    public CLSettlementParticipantCurrencyStateChange(Path<? extends CLSettlementParticipantCurrencyStateChange> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementParticipantCurrencyStateChange");
        addMetadata();
    }

    public CLSettlementParticipantCurrencyStateChange(PathMetadata metadata) {
        super(CLSettlementParticipantCurrencyStateChange.class, metadata, "null", "settlementParticipantCurrencyStateChange");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(6).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(externalReference, ColumnMetadata.named("externalReference").withIndex(5).ofType(Types.VARCHAR).withSize(50));
        addMetadata(reason, ColumnMetadata.named("reason").withIndex(4).ofType(Types.VARCHAR).withSize(512));
        addMetadata(settlementParticipantCurrencyId, ColumnMetadata.named("settlementParticipantCurrencyId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementParticipantCurrencyStateChangeId, ColumnMetadata.named("settlementParticipantCurrencyStateChangeId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementStateId, ColumnMetadata.named("settlementStateId").withIndex(3).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

